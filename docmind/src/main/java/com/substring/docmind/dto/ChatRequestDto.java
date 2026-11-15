package com.substring.docmind.dto;

import com.substring.docmind.dto.CitationDto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRequestDto {

    @NotBlank(message = "Question cannot be null")
    private String Question;
    private UUID documentId;
    private Integer topK;
    private Double minSimilarity;
    private String conversationId;
}
