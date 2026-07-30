package com.actuation_system.atendimento.dto;

import com.actuation_system.atendimento.StatusAtendimento;
import com.actuation_system.atendimento.TipoAtendimento;

import java.time.LocalDateTime;

public record AtendimentoResponseDTO(
        Long id,
        Long usuarioId,
        LocalDateTime horario,
        StatusAtendimento status,
        TipoAtendimento tipoAtendimento,
        Long mesaId
) {
}
