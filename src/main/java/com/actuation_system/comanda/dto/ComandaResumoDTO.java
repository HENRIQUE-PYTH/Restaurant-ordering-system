package com.actuation_system.comanda.dto;

import com.actuation_system.comanda.StatusComanda;

import java.time.LocalDateTime;

public record ComandaResumoDTO(
        Long id,
        StatusComanda status,
        LocalDateTime abertura,
        LocalDateTime fechamento,
        Long mesaId,
        Long usuarioId
) {}
