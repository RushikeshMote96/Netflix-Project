package com.Netflix.exceptions;

public class InvalidProfileException extends RuntimeException {
    public InvalidProfileException(final String message){
        super(message);
    }

}
