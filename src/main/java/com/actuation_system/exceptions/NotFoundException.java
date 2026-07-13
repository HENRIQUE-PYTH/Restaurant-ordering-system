package com.actuation_system.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String ms){
        super(ms);
    }
}
