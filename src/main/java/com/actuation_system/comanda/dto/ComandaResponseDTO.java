package com.actuation_system.comanda.dto;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.pedido.dto.ItemPedidoResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ComandaResponseDTO(
        Long id,
        StatusComanda status,
        LocalDateTime abertura,
        LocalDateTime fechamento,
        Long mesaId,
        Long usuarioId,
        List<ItemPedidoResponseDTO> pedidos // ou um resumo, dependendo do que o front precisa
) {}
