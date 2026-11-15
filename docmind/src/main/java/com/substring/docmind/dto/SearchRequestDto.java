package com.substring.docmind.dto;

import com.drew.lang.annotations.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDto {
    @NotBlank(message = "Query cannot be empty")
    private String query;

    private UUID documentId;
    private Integer topK;
    private Double similarityScore;

}
