package com.substring.docmind.dto;


import com.substring.docmind.enums.DocumentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentMetadataDto {

    private UUID id;
    private String filename;


    private String contentType;

    private Long fileSize;

     private Integer totalPages;

     private Integer totalChunks;

    private DocumentStatus status;

    private String errorMessage;

    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}
