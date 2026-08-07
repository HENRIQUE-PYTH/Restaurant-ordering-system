package com.actuation_system.pedido.dto;

import com.actuation_system.pedido.StatusPedido;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        Long comandaId,
        @JsonFormat(pattern = " yyyy-MM-dd'T'HH:mm:ss ")
        LocalDateTime horario,
        StatusPedido statusPedido,
        List<ItemPedidoResponseDTO> itens,
        BigDecimal total
) {}
