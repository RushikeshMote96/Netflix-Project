package com.Netflix.exceptions;

public class InvalidDataException extends RuntimeException{
    private String message;
    public InvalidDataException(String message){
        this.message = message;
    }
    @Override
    public String getMessage(){
        return this.message;
    }
}
