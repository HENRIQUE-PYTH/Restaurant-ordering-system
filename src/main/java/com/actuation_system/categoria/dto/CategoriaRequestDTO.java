package com.actuation_system.categoria.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDTO(
        @NotBlank
        String nome
) {}
