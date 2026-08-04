package com.actuation_system.pedido.controller;

import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.pedido.dto.AlterarQuantidadeRequestDTO;
import com.actuation_system.pedido.dto.ItemPedidoRequestDTO;
import com.actuation_system.pedido.dto.PedidoRequestDTO;
import com.actuation_system.pedido.dto.PedidoResponseDTO;
import com.actuation_system.pedido.entity.ItemPedido;
import com.actuation_system.pedido.entity.Pedido;
import com.actuation_system.pedido.mapper.ItemPedidoMapper;
import com.actuation_system.pedido.mapper.PedidoMapper;
import com.actuation_system.pedido.service.PedidoService;
import com.actuation_system.produto.entity.Produto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ItemPedidoMapper itemPedidoMapper;
    private final PedidoMapper pedidoMapper;


    @GetMapping
    public ResponseEntity<Page<PedidoResponseDTO>> getAll (
            @PageableDefault (size = 20, sort = "horario") Pageable pageable){
        Page<Pedido> pedidos = pedidoService.getAll(pageable);
        Page<PedidoResponseDTO> responses = pedidos.map(pedidoMapper::toResponse);
        return ResponseEntity.ok(responses);

    }

    @GetMapping("/{pedidoId}/")
    public ResponseEntity<PedidoResponseDTO> findById(@PathVariable Long pedidoId){
        Pedido pedido = pedidoService.findById(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> create(@Valid @RequestBody PedidoRequestDTO dto) {
        Pedido pedido = pedidoMapper.toEntity(dto);

        Comanda comandaRef = new Comanda();
        comandaRef.setId(dto.comandaId());
        pedido.setComanda(comandaRef);

        Pedido salvo = pedidoService.create(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoMapper.toResponse(salvo));
    }

    @PatchMapping("/pedido/{pedidoId}/itens/{itemId}")
    public ResponseEntity<PedidoResponseDTO> changeQuantity(
            @PathVariable Long pedidoId,
            @PathVariable Long itemId,
            @RequestBody @Valid AlterarQuantidadeRequestDTO dto){

        Pedido pedido = pedidoService.updateItemQuantity(
                pedidoId,
                itemId,
                dto.quantidade()
        );
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }

    @Transactional
    @PatchMapping("/pedido/{pedidoId}/iniciar-preparo")
    public ResponseEntity<PedidoResponseDTO> startPreparation (@PathVariable Long pedidoId){
        Pedido preparo = pedidoService.startPreparation(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(preparo));
    }

    @PatchMapping("/pedido/{pedidoId}/marcar-pronto")
    public ResponseEntity<PedidoResponseDTO> markAsDone (@PathVariable Long pedidoId){
        Pedido marcar = pedidoService.markAsDone(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(marcar));
    }

    @PatchMapping("/pedido/{pedidoId}/marcar-entregue")
    public ResponseEntity<PedidoResponseDTO> markAsDelivered (@PathVariable Long pedidoId){
        Pedido entregue = pedidoService.markAsDelivered(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(entregue));
    }

    @PatchMapping("/pedido/{pedidoId}/finalizar")
    public ResponseEntity<PedidoResponseDTO> finish (@PathVariable Long pedidoId){
        Pedido finalizar = pedidoService.finish(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(finalizar));
    }

    @PatchMapping("/pedido/{pedidoId}/cancelar")
    public ResponseEntity<PedidoResponseDTO> cancel (@PathVariable Long pedidoId){
        Pedido cancelar = pedidoService.cancel(pedidoId);
        return ResponseEntity.ok(pedidoMapper.toResponse(cancelar));
    }

    @PostMapping("/{pedidoId}/itens")
    public ResponseEntity<PedidoResponseDTO> addItem(
            @PathVariable Long pedidoId,
            @Valid @RequestBody ItemPedidoRequestDTO dto) {

        ItemPedido itemPedido = itemPedidoMapper.toEntity(dto);

        Produto produtoRef = new Produto();
        produtoRef.setId(dto.produtoId());
        itemPedido.setProduto(produtoRef);

        Pedido pedido = pedidoService.addItem(pedidoId, itemPedido);
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }

}
