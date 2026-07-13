package com.actuation_system.exceptions;


import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> NotFoundHandle(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), 404));
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> NoContentHandle(NoContentException ex){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ErrorResponse(ex.getMessage(), 204));
    }

    @ExceptionHandler(ConflictRequestException.class)
    public ResponseEntity<ErrorResponse> ConflictRequestHandle (ConflictRequestException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage(), 409));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> BadRequestHandle (BadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage(), 400));
    }
}
