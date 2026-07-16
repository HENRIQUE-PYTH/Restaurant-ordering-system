package com.actuation_system.categoria.mapper;

import com.actuation_system.categoria.dto.CategoriaRequestDTO;
import com.actuation_system.categoria.dto.CategoriaResponseDTO;
import com.actuation_system.categoria.entity.Categoria;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface CategoriaMapper {

    Categoria toEntity (CategoriaRequestDTO dto);

    CategoriaResponseDTO toResponse (Categoria categoria);
}
