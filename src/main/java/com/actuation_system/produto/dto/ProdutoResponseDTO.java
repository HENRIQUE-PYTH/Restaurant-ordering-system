package com.actuation_system.produto.dto;



import java.math.BigDecimal;

public record ProdutoResponseDTO(

        Long id,
        String name,
        BigDecimal preco
) {
}
