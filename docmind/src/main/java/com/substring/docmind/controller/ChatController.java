package com.substring.docmind.controller;

import com.substring.docmind.dto.ApiResponse;
import com.substring.docmind.dto.ChatRequestDto;
import com.substring.docmind.dto.ChatResponseDto;
import com.substring.docmind.dto.SearchRequestDto;
import com.substring.docmind.dto.SearchResultDto;
import com.substring.docmind.service.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/chat")
@Tag(
        name = "Chat",
        description = "Endpoints for asking questions over uploaded documents (RAG) and searching similar chunks"
)
@RequiredArgsConstructor
public class ChatController {

    private final RagService ragService;

    @PostMapping("/query")
    @Operation(
            summary = "Ask a question",
            description = "Retrieves the most relevant document chunks from the vector store and "
                    + "returns a complete answer generated from them."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Answer generated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body (validation failed)",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while generating the answer",
                    content = @Content
            )
    })
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
    @Operation(
            summary = "Ask a question (streaming)",
            description = "Same as /query, but the answer is streamed back token by token "
                    + "instead of being returned all at once."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Answer streamed successfully",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string"))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body (validation failed)",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while streaming the answer",
                    content = @Content
            )
    })
    public Flux<String> streamQuestion(
            @Valid @RequestBody ChatRequestDto request
    ) {
        return ragService.streamQuestionAnswer(request);
    }

    @PostMapping("/search/similarity")
    @Operation(
            summary = "Search similar chunks",
            description = "Runs a similarity search against the vector store and returns the "
                    + "closest matching document chunks, without generating an answer."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Similar chunks fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body (validation failed)",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during similarity search",
                    content = @Content
            )
    })
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