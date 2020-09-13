package com.gabor.hr.exception;

public class InvalidCredentials extends RuntimeException {
    public InvalidCredentials(String message) {
        super(message);
    }

    public InvalidCredentials(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCredentials(Throwable cause) {
        super(cause);
    }
}
