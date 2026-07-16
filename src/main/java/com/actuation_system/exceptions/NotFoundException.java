package com.actuation_system.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String ms){
        super(ms);
    }

    public NotFoundException(String ms, Throwable cause){
        super(ms, cause);
    }
}
