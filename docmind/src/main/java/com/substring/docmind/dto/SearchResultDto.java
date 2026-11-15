package com.substring.docmind.dto;

import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto {

    private String query;
    private int totalMatches;
    private List<CitationDto> matches;
}
