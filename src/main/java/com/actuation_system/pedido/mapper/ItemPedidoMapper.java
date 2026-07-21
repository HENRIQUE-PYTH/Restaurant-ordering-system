package com.actuation_system.pedido.mapper;

import com.actuation_system.pedido.dto.ItemPedidoRequestDTO;
import com.actuation_system.pedido.dto.ItemPedidoResponseDTO;
import com.actuation_system.pedido.entity.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    @Mapping(target = "nomeProduto", source = "produto.name")
    ItemPedidoResponseDTO toResponse(ItemPedido item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target = "precoUnitario", ignore = true)
    @Mapping(target = "pedido", ignore = true)
    ItemPedido toEntity(ItemPedidoRequestDTO dto);
}
