package com.actuation_system.exceptions;


import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> NotFoundHandle(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), 404));
    }


    @ExceptionHandler(NoContentException.class)
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

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorResponse> handleConflict(OptimisticLockException ex) {
        log.warn("Concurrency conflict detected: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        "This order was already modified by someone else. Refresh the screen and try again.",
                        HttpStatus.CONFLICT.value()
                ));
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ErrorResponse> handleTooManyRequests(TooManyRequestsException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorResponse(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS.value()));
    }
}
