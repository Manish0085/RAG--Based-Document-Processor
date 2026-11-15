package com.substring.docmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request body for a similarity search over document chunks")
public class SearchRequestDto {

    @NotBlank(message = "Query cannot be empty")
    @Schema(description = "Text to search for", example = "refund policy", requiredMode = Schema.RequiredMode.REQUIRED)
    private String query;

    @Schema(description = "Restrict the search to a single document. If omitted, all documents are searched.",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID documentId;

    @Schema(description = "Maximum number of matching chunks to return", example = "5")
    private Integer topK;

    @Schema(description = "Minimum similarity score (0.0 to 1.0) a chunk must have to be returned", example = "0.7")
    private Double similarityScore;
}