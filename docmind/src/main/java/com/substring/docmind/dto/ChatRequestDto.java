package com.substring.docmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request body for asking a question over the uploaded documents")
public class ChatRequestDto {

    @NotBlank(message = "Question cannot be blank")
    @Schema(description = "The question to ask", example = "What is the refund policy?", requiredMode = Schema.RequiredMode.REQUIRED)
    private String question;

    @Schema(description = "Restrict the search to a single document. If omitted, all documents are searched.",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID documentId;

    @Schema(description = "Number of most similar chunks to retrieve as context", example = "5")
    private Integer topK;

    @Schema(description = "Minimum similarity score (0.0 to 1.0) a chunk must have to be used", example = "0.7")
    private Double minSimilarity;

    @Schema(description = "Conversation ID to continue an existing chat. Leave empty to start a new one.",
            example = "conv-12345")
    private String conversationId;
}