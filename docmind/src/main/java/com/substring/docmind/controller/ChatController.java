package com.substring.docmind.controller;

import com.substring.docmind.dto.*;
import com.substring.docmind.service.RagService;
import jakarta.validation.Valid;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private RagService ragService;

    @PostMapping("/query")
    public ResponseEntity<ApiResponse<ChatResponseDto>> askQuestion(
            @Valid @RequestBody ChatRequestDto request
            ) {
        ChatResponseDto response = ragService.askQuestion(request);
        return ResponseEntity.ok(
                ApiResponse.<ChatResponseDto>
                builder()
                        .message(null)
                        .success(true)
                        .data(response)
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    @PostMapping("/stream")
    public Flux<String> streamQuestion(
            @Valid @RequestBody ChatRequestDto request
    ) {
        return ragService.streamQuestionAnswer(request);
    }


    @PostMapping("/search/similarity")
    public ResponseEntity<ApiResponse<SearchResultDto>> searchSimilar(
            @Valid @RequestBody SearchRequestDto request
    ) {
        SearchResultDto result = ragService.searchSimilarChunks(request);
        return ResponseEntity.ok(ApiResponse.<SearchResultDto>builder()
                        .message(null)
                        .data(result)
                        .timeStamp(LocalDateTime.now())
                        .success(true)
                .build());
    }

}
