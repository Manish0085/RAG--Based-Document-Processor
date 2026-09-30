package com.substring.docmind.service;

import com.substring.docmind.dto.DocumentResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentMetadataService {


    // Method to parse and upload document
    public DocumentResponseDto uploadAndProcess(MultipartFile file);
}
