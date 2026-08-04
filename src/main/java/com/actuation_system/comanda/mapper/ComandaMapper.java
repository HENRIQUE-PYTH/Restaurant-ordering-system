package com.actuation_system.comanda.mapper;

import com.actuation_system.comanda.dto.ComandaRequestDTO;
import com.actuation_system.comanda.dto.ComandaResponseDTO;
import com.actuation_system.comanda.dto.ComandaResumoDTO;
import com.actuation_system.comanda.entity.Comanda;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComandaMapper {

    @Mapping(target = "mesaId", source = "mesa.id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    ComandaResumoDTO toResumo(Comanda comanda);

    @Mapping(target = "mesaId", source = "mesa.id")
    @Mapping(target = "usuarioId", source = "usuario.id")
    ComandaResponseDTO toResponse(Comanda comanda);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "abertura", ignore = true)
    @Mapping(target = "fechamento", ignore = true)
    @Mapping(target = "mesa", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    Comanda toEntity(ComandaRequestDTO dto);
}
