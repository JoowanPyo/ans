package com.gemiso.zodiac.exception;

public class PasswordFailedException extends RuntimeException {
    public PasswordFailedException(String message) {
        super(message);
    }
}
