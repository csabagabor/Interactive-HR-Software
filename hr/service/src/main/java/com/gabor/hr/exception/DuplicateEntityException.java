package com.gabor.hr.exception;

public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException(String exception) {
        super(exception);
    }

    public DuplicateEntityException() {
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
