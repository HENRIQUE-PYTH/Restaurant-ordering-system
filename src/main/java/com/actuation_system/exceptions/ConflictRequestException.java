package com.actuation_system.exceptions;

public class ConflictRequestException extends RuntimeException{
    public ConflictRequestException(String ms){
        super(ms);
    }
}
