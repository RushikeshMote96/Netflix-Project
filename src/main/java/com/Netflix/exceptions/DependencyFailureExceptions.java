package com.Netflix.exceptions;

public class DependencyFailureExceptions extends RuntimeException{
    public DependencyFailureExceptions(Throwable cause){
        super(cause);
    }
}
