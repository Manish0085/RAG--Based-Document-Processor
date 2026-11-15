package com.substring.docmind.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
public class DocumentsProcessingException extends RuntimeException {

    public DocumentsProcessingException(String message) {
        super(message);
    }

    public DocumentsProcessingException() {
        super("Error in processing document !!");
    }

    public DocumentsProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
