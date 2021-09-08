package com.gemiso.zodiac.exception;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
