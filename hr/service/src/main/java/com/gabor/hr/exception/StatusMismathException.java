package com.gabor.hr.exception;

public class StatusMismathException extends RuntimeException {

    public StatusMismathException(String exception) {
        super(exception);
    }

    public StatusMismathException() {
    }

    public StatusMismathException(String message, Throwable cause) {
        super(message, cause);
    }
}
