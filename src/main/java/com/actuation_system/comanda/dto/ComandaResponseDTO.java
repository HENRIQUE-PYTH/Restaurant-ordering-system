package com.actuation_system.comanda.dto;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.pedido.dto.ItemPedidoResponseDTO;
import com.actuation_system.pedido.dto.PedidoResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ComandaResponseDTO(

        Long id,
        StatusComanda status,

        @JsonFormat(pattern = " yyyy-MM-dd'T'HH:mm:ss ")
        LocalDateTime abertura,

        @JsonFormat(pattern = " yyyy-MM-dd'T'HH:mm:ss ")
        LocalDateTime fechamento,

        Long mesaId,
        Long usuarioId,
        List<PedidoResponseDTO> pedidos // ou um resumo, dependendo do que o front precisa
) {}
