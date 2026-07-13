package com.actuation_system.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String ms){
        super(ms);
    }
}
