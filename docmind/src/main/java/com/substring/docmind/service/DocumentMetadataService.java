package com.substring.docmind.service;

import com.substring.docmind.dto.DocumentMetadataDto;
import com.substring.docmind.dto.DocumentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DocumentMetadataService {


    // Method to parse and upload document
    public DocumentResponseDto uploadAndProcess(MultipartFile file);

    List<DocumentResponseDto> uploadMultipleDocuments(List<MultipartFile> files);

    List<DocumentMetadataDto> getAllDocuments();

    DocumentMetadataDto getDocumentById(UUID id);

    void deleteDocumentById(UUID id);
}
