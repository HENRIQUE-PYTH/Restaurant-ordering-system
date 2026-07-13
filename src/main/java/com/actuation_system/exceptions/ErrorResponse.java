package com.actuation_system.exceptions;

import java.time.LocalDateTime;

public class ErrorResponse {
    private int status;
    private String mensagem;
    private LocalDateTime timestamp;

    public ErrorResponse(String mensagem, int status) {
        this.mensagem = mensagem;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
