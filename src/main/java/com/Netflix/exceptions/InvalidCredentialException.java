package com.Netflix.exceptions;

public class InvalidCredentialException extends RuntimeException{
    private String message;
    public InvalidCredentialException(final String message){
        this.message=message;
    }
    @Override
    public String getMessage(){
        return this.message;
    }
}
