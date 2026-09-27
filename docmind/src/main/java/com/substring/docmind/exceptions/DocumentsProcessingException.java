package com.substring.docmind.exceptions;


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
