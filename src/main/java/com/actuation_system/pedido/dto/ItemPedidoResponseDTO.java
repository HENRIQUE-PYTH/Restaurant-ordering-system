package com.actuation_system.pedido.dto;

public record ItemPedidoResponseDTO(
        Long id,
        String nomeProduto,
        Integer quantidade,
        Double precoUnitario,
        String observacao
) {}
