package com.substring.docmind.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documents")
@Tag(
        name = "Document Management",
        description = "Endpoints for uploading, listing and managing documents and their vectors embeddings"
)
public class DocumentController {


    @PostMapping
    @Operation(summary = "Upload and index documents(DOCX, PDF, TEXT, CSV, MD")
    public ResponseEntity<String> uploadDocument() {
        return ResponseEntity.ok("upload");
    }



}
