package com.substring.docmind.service.impl;

import com.substring.docmind.dto.DocumentMetadataDto;
import com.substring.docmind.dto.DocumentResponseDto;
import com.substring.docmind.entity.DocumentMetadata;
import com.substring.docmind.enums.DocumentStatus;
import com.substring.docmind.exceptions.DocumentsProcessingException;
import com.substring.docmind.exceptions.ResourceNotFoundException;
import com.substring.docmind.repository.DocumentMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentMetadataService implements com.substring.docmind.service.DocumentMetadataService {


    private static final Logger log = LoggerFactory.getLogger(DocumentMetadataService.class);
    private final DocumentMetadataRepository documentMetadataRepository;
    private final DocumentParserService parseService;
    private final DocumentIngestionService ingestionService;
    private final JdbcTemplate jdbcTemplate;
    private final ModelMapper mapper;
    @Override
    public DocumentResponseDto uploadAndProcess(MultipartFile file) {
        String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document";
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        // Create Document Meta data
        DocumentMetadata documentMetadata = DocumentMetadata
                .builder()
                .fileSize(file.getSize())
                .filename(fileName)
                .contentType(contentType)
                .status(DocumentStatus.UPLOADING)
                .createdAt(LocalDateTime.now())
                .build();


        // save the document metadata
        documentMetadata = documentMetadataRepository.save(documentMetadata);

        // parse the file
        List<Document> parsedDocs = null;

        //  ingest service
        int chunkCreated = 0;
        try {
            parsedDocs = parseService.parse(file);

            chunkCreated = ingestionService.ingest(documentMetadata, parsedDocs);
        }
        catch (DocumentsProcessingException e) {
            documentMetadataRepository.delete(documentMetadata);

            log.warn("Deleting DocumentMetadata due to fail processing");
            throw e;
        }

        // documentMetadata.setTotalChunks(chunkCreated);



        return DocumentResponseDto.builder()
                .id(documentMetadata.getId())
                .filename(documentMetadata.getFilename())
                .fileSize(documentMetadata.getFileSize())
                .chunkCreated(chunkCreated)
                .status(documentMetadata.getStatus())
                .message("Document Successfully processed and chunked")
                .build();
    }

    @Override
    public List<DocumentResponseDto> uploadMultipleDocuments(List<MultipartFile> files) {
        List<DocumentResponseDto> responseDto = new ArrayList<>();
        for (MultipartFile file: files) {
            DocumentResponseDto result = this.uploadAndProcess(file);
            responseDto.add(result);
        }

        return responseDto;
    }

    @Override
    public List<DocumentMetadataDto> getAllDocuments() {
        List<DocumentMetadata> documents = documentMetadataRepository.findAllByOrderByCreatedAtDesc();
        return documents.stream()
                .map(documentMetadata -> mapper.map(documentMetadata, DocumentMetadataDto.class))
                .toList();
    }


    @Override
    public DocumentMetadataDto getDocumentById(UUID id) {
        DocumentMetadata document = documentMetadataRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Document with given Id not found!!"));
        return mapper.map(document, DocumentMetadataDto.class);
    }

    @Override
    public void deleteDocumentById(UUID id) {
        DocumentMetadata document = documentMetadataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document with given Id not found"));

        // delete vector corresponding to this document also
        try {
            String deleteVectorsSql = "DELETE FROM vector_store WHERE metadata->>'documentId' = ?";
            int deletedCount = jdbcTemplate.update(deleteVectorsSql, id.toString());
            log.info("Delete {} vector chunks for document id {}", deletedCount, id);
        } catch (Exception e) {
            log.warn("could not delete vectors from vector store directly: {}", e.getMessage());
        }
    }


}
