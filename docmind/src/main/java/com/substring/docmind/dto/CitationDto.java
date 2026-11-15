package com.substring.docmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "A document chunk that was used as a source for an answer")
public class CitationDto {

    @Schema(description = "ID of the source document", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID documentId;

    @Schema(description = "Original file name of the source document", example = "policy.pdf")
    private String fileName;

    @Schema(description = "Index of the chunk within the document", example = "4")
    private Integer chunkIndex;

    @Schema(description = "Page number the chunk comes from, if available", example = "2")
    private Integer pageNumber;

    @Schema(description = "Short excerpt of the matching text", example = "Customers may request a refund within 30 days...")
    private String snippet;

    @Schema(description = "Similarity score between the question and this chunk (higher is closer)", example = "0.87")
    private Double similarityScore;

    @Schema(description = "Additional metadata stored with the chunk")
    private Map<String, Object> metadata;
}