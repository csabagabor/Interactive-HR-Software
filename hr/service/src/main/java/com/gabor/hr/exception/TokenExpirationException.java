package com.gabor.hr.exception;

public class TokenExpirationException extends RuntimeException {

    public TokenExpirationException(String exception) {
        super(exception);
    }
}