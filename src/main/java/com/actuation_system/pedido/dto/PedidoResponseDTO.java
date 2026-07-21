package com.actuation_system.pedido.dto;

import com.actuation_system.pedido.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        Integer comanda,
        LocalDateTime horario,
        StatusPedido statusPedido,
        List<ItemPedidoResponseDTO> itens,
        Double total
) {}
