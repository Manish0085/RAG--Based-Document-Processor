package com.substring.docmind.service.impl;

import com.substring.docmind.config.AppProperties;
import com.substring.docmind.entity.DocumentMetadata;
import com.substring.docmind.enums.DocumentStatus;
import com.substring.docmind.exceptions.DocumentsProcessingException;
import com.substring.docmind.repository.DocumentMetadataRepository;
import com.substring.docmind.service.IDocumentIngestionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DocumentIngestionService implements IDocumentIngestionService {

    private static final Logger log = LoggerFactory.getLogger(DocumentIngestionService.class);
    private final VectorStore vectorStore;
    private final DocumentMetadataRepository documentMetadataRepository;
    private final AppProperties appProperties;

    @Override
    public int ingest(DocumentMetadata metadata, List<Document> parseDocs) {

        log.info("Ingesting document [id={}, name={}, pages={}]", metadata.getId(), metadata.getFilename(), metadata.getFileSize());

        try {
            metadata.setStatus(DocumentStatus.PROCESSING);
            metadata.setTotalPages(parseDocs.size());

            // Text Chunking using TokenTextSplitter
            TokenTextSplitter textSplitter = TokenTextSplitter.builder()
                    .withChunkSize(appProperties.getRag().getChunkSize())
                    .withMinChunkSizeChars(appProperties.getRag().getMinChunkSizeChars())
                    .withMinChunkLengthToEmbed(appProperties.getRag().getMinChunkLengthToEmbed())
                    .withMaxNumChunks(appProperties.getRag().getMaxNumChunks())
                    .withKeepSeparator(true)
                    .build();

            List<Document> chunks = textSplitter.apply(parseDocs);

            if (chunks.isEmpty() || chunks.size() == 0) {
                metadata.setStatus(DocumentStatus.FAILED);
                metadata.setErrorMessage("Document appears to be empty or unscannable");
                documentMetadataRepository.save(metadata);
                return 0;
            }

            // Metadata enrichment on each chunk
            List<Document> enrichedChunks = new ArrayList<>();
            for (int i = 0; i < chunks.size(); i++) {
                Document chunk = chunks.get(i);
                Map<String, Object> enrichedMetadata = new HashMap<>(chunk.getMetadata());
                enrichedMetadata.put("documentId", metadata.getId().toString());
                enrichedMetadata.put("fileName", metadata.getFilename());
                enrichedMetadata.put("contentType", metadata.getContentType());
                enrichedMetadata.put("chunkIndex", i);

                // Preserve or calculate page number if available
                Object pageNumber = chunk.getMetadata().get("page_number");
                if (pageNumber == null) {
                    pageNumber = chunk.getMetadata().get("pageNumber");
                }
                if (pageNumber != null) {
                    enrichedMetadata.put("pageNumber", pageNumber);
                }

                Document enrichedDocs = new Document(chunk.getText(), enrichedMetadata);
                enrichedChunks.add(enrichedDocs);
            }

            // Write chunks and embeddings to pg vector
            log.info("Writing {} vector chunks to PgVectorStore for document {}", enrichedChunks.size(), metadata.getFilename());
            vectorStore.add(enrichedChunks);

            // update document status to index
            metadata.setStatus(DocumentStatus.INDEXED);
            metadata.setTotalChunks(enrichedChunks.size());
            metadata.setErrorMessage(null);
            documentMetadataRepository.save(metadata);
            log.info("Successfully indexed document [id={}, name={}, chunks={}]", metadata.getId(), metadata.getFilename(), enrichedChunks);

            return enrichedChunks.size();
        } catch(Exception exception) {
            log.info("Failed to ingest document into vector store: {}", metadata.getFilename(), exception);
            metadata.setErrorMessage(exception.getMessage());
            documentMetadataRepository.save(metadata);
            throw new DocumentsProcessingException("Failed to index document: " + exception.getMessage(), exception)
        }
        return 0;
    }
}
