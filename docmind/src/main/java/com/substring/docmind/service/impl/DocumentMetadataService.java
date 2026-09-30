package com.substring.docmind.service.impl;

import com.substring.docmind.dto.DocumentResponseDto;
import com.substring.docmind.entity.DocumentMetadata;
import com.substring.docmind.enums.DocumentStatus;
import com.substring.docmind.repository.DocumentMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentMetadataService implements com.substring.docmind.service.DocumentMetadataService {


    private static final Logger log = LoggerFactory.getLogger(DocumentMetadataService.class);
    private final DocumentMetadataRepository documentMetadataRepository;
    private final DocumentParserService ParserService;
    private final DocumentIngestionService ingestionService;
    private final JdbcTemplate jdbcTemplate;
    @Override
    public DocumentResponseDto uploadAndProcess(MultipartFile file) {
        String fileName = file.getName() != null ? file.getName() : "document";
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        // Create Document Meta data
        DocumentMetadata documentMetadata = DocumentMetadata
                .builder()
                .fileSize(file.getSize())
                .filename(fileName)
                .contentType(contentType)
                .status(DocumentStatus.UPLOADING)
                .build();


        // save the document metadata
        documentMetadata = documentMetadataRepository.save(documentMetadata);

        // parse the file
        List<Document> parsedDocs = parseService.parse(file);

        //  ingest service
        int chunkCreated = ingestionService.ingest(documentMetadata, parsedDocs);

        // documentMetadata.setTotalChunks(chunkCreated);



        return DocumentResponseDto.builder()
                .id(documentMetadata.getId())
                .filename(documentMetadata.getFilename())
                .fileSize(documentMetadata.getFileSize())
                .chunkCreated(chunkCreated)
                .status(documentMetadata.getStatus())
                .message("Document Successfully processed and chunked  ")
                .build();
    }
}
