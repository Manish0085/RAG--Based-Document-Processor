package com.substring.docmind.dto;

import com.substring.docmind.enums.DocumentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Result returned after a document is uploaded and processed")
public class DocumentResponseDto {

    @Schema(description = "Unique identifier assigned to the document", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Original file name", example = "policy.pdf")
    private String filename;

    @Schema(description = "File size in bytes", example = "245760")
    private Long fileSize;

    @Schema(description = "Processing status of the document")
    private DocumentStatus status;

    @Schema(description = "Number of chunks created from the document", example = "34")
    private Integer chunkCreated;

    @Schema(description = "Additional information about the upload", example = "Document processed successfully")
    private String message;
}