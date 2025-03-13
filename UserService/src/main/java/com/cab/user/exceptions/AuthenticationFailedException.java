package com.cab.user.exceptions;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException() {
        super();
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }
}