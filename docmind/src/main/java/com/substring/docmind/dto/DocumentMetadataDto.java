package com.substring.docmind.dto;

import com.substring.docmind.enums.DocumentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Stored metadata of an uploaded document")
public class DocumentMetadataDto {

    @Schema(description = "Unique identifier of the document", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Original file name", example = "policy.pdf")
    private String filename;

    @Schema(description = "MIME type of the file", example = "application/pdf")
    private String contentType;

    @Schema(description = "File size in bytes", example = "245760")
    private Long fileSize;

    @Schema(description = "Total number of pages, if applicable to the file type", example = "12")
    private Integer totalPages;

    @Schema(description = "Number of chunks the document was split into for embedding", example = "34")
    private Integer totalChunks;

    @Schema(description = "Current processing status of the document")
    private DocumentStatus status;

    @Schema(description = "Error details if processing failed; null otherwise", example = "Unsupported file type")
    private String errorMessage;

    @Schema(description = "When the document was uploaded", example = "2026-11-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "When the document was last updated", example = "2026-11-15T10:31:00")
    private LocalDateTime updatedAt;
}