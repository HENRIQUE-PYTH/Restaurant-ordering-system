package com.actuation_system.produto.mapper;

import com.actuation_system.produto.dto.ProdutoRequestDTO;
import com.actuation_system.produto.dto.ProdutoResponseDTO;
import com.actuation_system.produto.entity.Produto;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface ProdutoMapper {

    Produto toEntity (ProdutoRequestDTO dto);

    ProdutoResponseDTO toResponse (Produto produto);
}
