package com.substring.docmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Result of a similarity search")
public class SearchResultDto {

    @Schema(description = "The query that was searched", example = "refund policy")
    private String query;

    @Schema(description = "Total number of matching chunks", example = "3")
    private int totalMatches;

    @Schema(description = "Matching chunks, most similar first")
    private List<CitationDto> matches;
}