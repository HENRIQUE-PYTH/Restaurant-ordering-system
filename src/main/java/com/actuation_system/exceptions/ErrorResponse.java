package com.actuation_system.exceptions;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String mensagem;
    private LocalDateTime timestamp;

    public ErrorResponse(String mensagem, int status) {
        this.mensagem = mensagem;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }


}
