package com.actuation_system.pedido.controller;

import com.actuation_system.pedido.dto.ItemPedidoRequestDTO;
import com.actuation_system.pedido.dto.PedidoResponseDTO;
import com.actuation_system.pedido.entity.ItemPedido;
import com.actuation_system.pedido.entity.Pedido;
import com.actuation_system.pedido.mapper.ItemPedidoMapper;
import com.actuation_system.pedido.mapper.PedidoMapper;
import com.actuation_system.pedido.service.PedidoService;
import com.actuation_system.produto.entity.Produto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ItemPedidoMapper itemPedidoMapper;
    private final PedidoMapper pedidoMapper;



    @PostMapping("/{pedidoId}/itens")
    public ResponseEntity<PedidoResponseDTO> adicionarItem(
            @PathVariable Long pedidoId,
            @Valid @RequestBody ItemPedidoRequestDTO dto) {
        ItemPedido itemPedido = itemPedidoMapper.toEntity(dto);

        Produto produtoRef = new Produto();
        produtoRef.setId(dto.produtoId());
        itemPedido.setProduto(produtoRef);

        Pedido pedido = pedidoService.adicionarItem(pedidoId, itemPedido);
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }
}
