package com.actuation_system.atendimento.dto;

import com.actuation_system.atendimento.TipoAtendimento;

public record AtendimentoRequestDTO(
        Long mesaId,
        TipoAtendimento tipo
) {
}
