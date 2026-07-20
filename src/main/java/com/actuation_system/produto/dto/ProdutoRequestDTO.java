package com.actuation_system.produto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequestDTO(

        @NotBlank(message = "Required a name in field")
        String name,

        @NotNull(message = "Required a price in field")
        BigDecimal preco,

        Long categoryId
) {
}
