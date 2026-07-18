package com.actuation_system.mesa.mapper;

import com.actuation_system.mesa.dto.MesaRequestDTO;
import com.actuation_system.mesa.dto.MesaResponseDTO;
import com.actuation_system.mesa.entity.Mesa;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface MesaMapper {

    Mesa toEntity(MesaRequestDTO dto);

    MesaResponseDTO toResponse (Mesa mesa);

}
