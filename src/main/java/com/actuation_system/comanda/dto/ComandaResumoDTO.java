package com.actuation_system.comanda.dto;

import com.actuation_system.comanda.StatusComanda;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ComandaResumoDTO(
        Long id,
        StatusComanda status,

        @JsonFormat(pattern = " yyyy-MM-dd'T'HH:mm:ss ")
        LocalDateTime abertura,

        @JsonFormat(pattern = " yyyy-MM-dd'T'HH:mm:ss ")
        LocalDateTime fechamento,

        Long mesaId,
        Long usuarioId
) {}
