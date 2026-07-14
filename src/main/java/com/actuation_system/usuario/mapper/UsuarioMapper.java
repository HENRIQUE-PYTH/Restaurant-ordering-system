package com.actuation_system.usuario.mapper;

import com.actuation_system.usuario.dto.UsuarioRequestDTO;
import com.actuation_system.usuario.dto.UsuarioResponseDTO;
import com.actuation_system.usuario.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity (UsuarioRequestDTO dto){
        Usuario user = new Usuario();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(dto.getSenha());
        return user;
    }

    public UsuarioResponseDTO toResponse (Usuario user){
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
