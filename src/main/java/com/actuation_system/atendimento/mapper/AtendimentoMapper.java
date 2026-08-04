package com.actuation_system.atendimento.mapper;

import com.actuation_system.atendimento.dto.AtendimentoRequestDTO;
import com.actuation_system.atendimento.dto.AtendimentoResponseDTO;
import com.actuation_system.atendimento.entity.Atendimento;
import org.hibernate.boot.internal.Target;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AtendimentoMapper {

    @Mapping(target = "mesaId", source = "mesa.id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    AtendimentoResponseDTO toResponse(Atendimento atendimento);

    @Mapping(target = "tipoAtendimento", source = "tipo")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "horario", ignore = true)
    @Mapping(target = "mesa", ignore = true)
    Atendimento toEntity(AtendimentoRequestDTO dto);
}
