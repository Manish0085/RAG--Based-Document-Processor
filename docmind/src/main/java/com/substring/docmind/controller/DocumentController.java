package com.substring.docmind.controller;


import com.substring.docmind.dto.ApiResponse;
import com.substring.docmind.dto.DocumentMetadataDto;
import com.substring.docmind.dto.DocumentResponseDto;
import com.substring.docmind.service.DocumentMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@Tag(
        name = "Document Management",
        description = "Endpoints for uploading, listing and managing documents and their vectors embeddings"
)
@RequiredArgsConstructor
public class DocumentController {


    private final DocumentMetadataService documentMetadataService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload and index documents(DOCX, PDF, TEXT, CSV, MD")
    public ResponseEntity<ApiResponse <DocumentResponseDto>> uploadDocument(
            @RequestParam("file") MultipartFile file
    ) {
        DocumentResponseDto documentResponseDto = documentMetadataService.uploadAndProcess(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<DocumentResponseDto>builder()
                        .success(true)
                        .message("Document Uploaded successfully")
                        .data(documentResponseDto)
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    // upload multiple document
    @PostMapping(value = "/upload-multiple")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> uploadMultiple(
            @RequestParam("files") List<MultipartFile> files
    ) {
        List<DocumentResponseDto> responseDtos = documentMetadataService.uploadMultipleDocuments(files);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<List<DocumentResponseDto>>
                                builder()
                                .message("Document uploaded successfully")
                                .success(true)
                                .timeStamp(LocalDateTime.now())
                                .data(responseDtos)
                                .build()
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentMetadataDto>>> getAllDocuments() {
        List<DocumentMetadataDto> documents = documentMetadataService.getAllDocuments();
        return ResponseEntity.ok(
                ApiResponse.<List<DocumentMetadataDto>>
                        builder()
                        .message("All Documents have been fetched")
                        .success(true)
                        .timeStamp(LocalDateTime.now())
                        .data(documents)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentMetadataDto>> getDocumentById(@PathVariable UUID id) {
        DocumentMetadataDto document = documentMetadataService.getDocumentById(id);
        return ResponseEntity.ok(
                ApiResponse.<DocumentMetadataDto>
                                builder()
                        .message("Document has been fetched successfully")
                        .success(true)
                        .timeStamp(LocalDateTime.now())
                        .data(document)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDocumentById(@PathVariable UUID id) {
        documentMetadataService.deleteDocumentById(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>
                                builder()
                        .message("Document has been deleted successfully")
                        .success(true)
                        .timeStamp(LocalDateTime.now())
                        .data(null)
                        .build()
        );
    }
}
