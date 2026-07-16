package com.actuation_system.mesa.dto;

import jakarta.validation.constraints.NotNull;

public record MesaRequestDTO(

        @NotNull
        Integer numeroMesa

) {}
