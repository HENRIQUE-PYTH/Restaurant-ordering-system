package com.actuation_system.pedido.dto;

import jakarta.validation.constraints.NotNull;

public record PedidoRequestDTO(
        @NotNull(message = "A comanda é obrigatória")
        Integer comanda
) {}
