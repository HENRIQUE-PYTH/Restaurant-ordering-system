package com.actuation_system.pedido.mapper;

import com.actuation_system.pedido.dto.PedidoRequestDTO;
import com.actuation_system.pedido.dto.PedidoResponseDTO;
import com.actuation_system.pedido.entity.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {ItemPedidoMapper.class})
public interface PedidoMapper {

    @Mapping(target = "total", expression = "java(calcularTotal(pedido))")
    PedidoResponseDTO toResponse(Pedido pedido);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comanda", ignore = true)
    @Mapping(target = "itens", ignore = true)
    @Mapping(target = "statusPedido", ignore = true)
    Pedido toEntity (PedidoRequestDTO dto);

    default BigDecimal calcularTotal(Pedido pedido) {
        return pedido.getItens().stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
