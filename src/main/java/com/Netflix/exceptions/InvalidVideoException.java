package com.Netflix.exceptions;

public class InvalidVideoException extends RuntimeException{
    public InvalidVideoException (final String message){
    super(message);
    }
}
