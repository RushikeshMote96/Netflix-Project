package com.prepfortech.exceptions;

public class DependencyFailureExceptions extends RuntimeException{
    public DependencyFailureExceptions(Throwable cause){
        super(cause);
    }
}
