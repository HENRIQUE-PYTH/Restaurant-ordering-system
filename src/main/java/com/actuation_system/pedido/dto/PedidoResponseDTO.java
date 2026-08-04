package com.actuation_system.pedido.dto;

import com.actuation_system.pedido.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        Long comandaId,
        LocalDateTime horario,
        StatusPedido statusPedido,
        List<ItemPedidoResponseDTO> itens,
        BigDecimal total
) {}
