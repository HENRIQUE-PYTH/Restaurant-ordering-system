package com.actuation_system.pedido.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ItemPedidoRequestDTO(
        @NotNull(message = "The product is mandatory.")
        Long produtoId,

        @NotNull(message = "The quantity is mandatory.")
        @Positive(message = "The quantity must be greater than zero.")
        Integer quantidade,

        @Size(max = 250, message = "The remark must be a maximum of 250 characters.")
        String observacao
) {}
