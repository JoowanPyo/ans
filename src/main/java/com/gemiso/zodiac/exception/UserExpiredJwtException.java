package com.gemiso.zodiac.exception;

public class UserExpiredJwtException extends RuntimeException{

    public UserExpiredJwtException(String message) {
        super(message);
    }
}
