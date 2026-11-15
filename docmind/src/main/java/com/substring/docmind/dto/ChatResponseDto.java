package com.substring.docmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Answer generated from the uploaded documents, with its sources")
public class ChatResponseDto {

    @Schema(description = "The generated answer", example = "Refunds are available within 30 days of purchase.")
    private String answer;

    @Schema(description = "ID of the conversation this answer belongs to", example = "conv-12345")
    private String conversationId;

    @Schema(description = "Document chunks used to produce the answer")
    private List<CitationDto> citations;

    @Schema(description = "Time taken to produce the answer, in milliseconds", example = "1420")
    private Long responseTimeMs;
}