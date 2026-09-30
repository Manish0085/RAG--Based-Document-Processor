package com.substring.docmind.controller;


import com.substring.docmind.dto.ApiResponse;
import com.substring.docmind.dto.DocumentResponseDto;
import com.substring.docmind.service.DocumentMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/documents")
@Tag(
        name = "Document Management",
        description = "Endpoints for uploading, listing and managing documents and their vectors embeddings"
)
@RequiredArgsConstructor
public class DocumentController {


    private final DocumentMetadataService DocumentMetadataService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload and index documents(DOCX, PDF, TEXT, CSV, MD")
    public ResponseEntity<DocumentResponseDto> uploadDocument(
            @RequestParam("file") MultipartFile file
    ) {
        DocumentResponseDto documentResponseDto = DocumentMetadataService.uploadAndProcess(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<DocumentResponseDto>builder
                        .success(true)
                        .message("Document Uploaded successfully")
                        .data(documentResponseDto)
                        .timestamp(LocalDateTime.now())
                        .build());
    }



}
