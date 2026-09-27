package com.substring.docmind.exceptions;


import com.substring.docmind.dto.ApiResponse;
import jakarta.websocket.OnClose;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException exception) {
        logger.warn("Resource Not Found: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.builder()
                        .success(false)
                        .message("" + exception.getMessage())
                        .data(null)
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(DocumentsProcessingException.class)
    public ResponseEntity<ApiResponse<Object>> handleDocumentProcessingException(DocumentsProcessingException exception) {
        logger.warn("Document Processing Error: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ApiResponse.builder()
                        .success(false)
                        .message("Failed to process document: " + exception.getMessage())
                        .data(null)
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        logger.warn("File size limit exception: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(ApiResponse.builder()
                        .success(false)
                        .message("File size exceeds the allowed limit (25MB) " + exception.getMessage())
                        .data(null)
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        logger.warn("File size limit exception: {}", exception.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError error: exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Map<String, String>>builder()
                        .success(false)
                        .message("Validation Error")
                        .data(errors)
                        .timeStamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException exception) {
        logger.warn("Illegal Argument: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder()
                        .success(false)
                        .message("Illegal Argument Error" + exception.getMessage())
                        .data(null)
                        .timeStamp(LocalDateTime.now())
                        .build());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(IllegalArgumentException exception) {
        logger.warn("Illegal Argument: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.builder()
                        .success(false)
                        .message("An Unexpected error occurred" + exception.getMessage())
                        .data(null)
                        .timeStamp(LocalDateTime.now())
                        .build());
    }
}
