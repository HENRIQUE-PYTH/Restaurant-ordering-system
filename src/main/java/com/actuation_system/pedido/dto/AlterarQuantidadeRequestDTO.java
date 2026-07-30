package com.actuation_system.pedido.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record AlterarQuantidadeRequestDTO(
        @NotNull
        @PositiveOrZero
        Integer quantidade
) {}
