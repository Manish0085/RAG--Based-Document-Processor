package com.substring.docmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Standard wrapper for every API response")
public class ApiResponse<T> {

    @Schema(description = "Whether the request was processed successfully", example = "true")
    private Boolean success;

    @Schema(description = "Human-readable message about the result", example = "Document has been fetched successfully")
    private String message;

    @Schema(description = "Response payload; its type depends on the endpoint")
    private T data;

    @Schema(description = "Time the response was generated", example = "2026-11-15T10:30:00")
    private LocalDateTime timeStamp;
}