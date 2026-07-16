package com.actuation_system.usuario.mapper;

import com.actuation_system.usuario.dto.UsuarioRequestDTO;
import com.actuation_system.usuario.dto.UsuarioResponseDTO;
import com.actuation_system.usuario.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity (UsuarioRequestDTO dto);

    UsuarioResponseDTO toResponse (Usuario usuario);
}
