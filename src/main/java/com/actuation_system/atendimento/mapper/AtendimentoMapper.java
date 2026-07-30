package com.actuation_system.atendimento.mapper;

import com.actuation_system.atendimento.dto.AtendimentoRequestDTO;
import com.actuation_system.atendimento.dto.AtendimentoResponseDTO;
import com.actuation_system.atendimento.entity.Atendimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AtendimentoMapper {

    AtendimentoResponseDTO toResponse(Atendimento atendimento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "horario", ignore = true)
    Atendimento toEntity(AtendimentoRequestDTO dto);
}
