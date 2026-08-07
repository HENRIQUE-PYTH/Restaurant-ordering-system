package com.actuation_system.atendimento.dto;

import com.actuation_system.atendimento.StatusAtendimento;
import com.actuation_system.atendimento.TipoAtendimento;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AtendimentoResponseDTO(
        Long id,
        Long usuarioId,

        @JsonFormat(pattern = " yyyy-MM-dd'T'HH:mm:ss ")
        LocalDateTime horario,

        StatusAtendimento status,
        TipoAtendimento tipoAtendimento,
        Long mesaId
) {
}
