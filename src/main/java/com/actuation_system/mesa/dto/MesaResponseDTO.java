package com.actuation_system.mesa.dto;

import com.actuation_system.mesa.StatusMesa;

public record MesaResponseDTO(

        Long id,
        Integer numeroMesa,
        StatusMesa status,
        String qrCodeToken

) {}
