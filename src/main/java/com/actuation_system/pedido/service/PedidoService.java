package com.actuation_system.pedido.service;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.comanda.repository.ComandaRepository;
import com.actuation_system.exceptions.BadRequestException;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.mesa.repository.MesaRepository;
import com.actuation_system.pedido.StatusPedido;
import com.actuation_system.pedido.entity.ItemPedido;
import com.actuation_system.pedido.entity.Pedido;
import com.actuation_system.pedido.mapper.PedidoMapper;
import com.actuation_system.pedido.repositorio.ItemPedidoRepository;
import com.actuation_system.pedido.repositorio.PedidoRepository;
import com.actuation_system.produto.entity.Produto;
import com.actuation_system.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ComandaRepository comandaRepository;
    private final ProdutoService produtoService;
    private final PedidoMapper mapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final MesaRepository mesaRepository;

    public Page<Pedido> getAll (Pageable pageable){
        return pedidoRepository.findAll(pageable);
    }

    public Pedido findById (Long id){
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public Pedido create(Pedido pedido) {
        Comanda comanda = comandaRepository.findById(pedido.getComanda().getId())
                .orElseThrow(() -> new NotFoundException("Comanda não encontrada"));

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new BadRequestException("Não é possível criar pedido em uma comanda que não está aberta");
        }

        pedido.setComanda(comanda);
        pedido.setStatusPedido(StatusPedido.CRIADO);
        pedido.setHorario(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido addItem(Long pedidoId, ItemPedido itemPedido) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (pedido.getStatusPedido() != StatusPedido.CRIADO) {
            throw new BadRequestException("It is only possible to add items to an order that has not yet been sent to the kitchen.");
        }

        Optional<ItemPedido> itemExistente = pedido.getItens().stream()
                .filter(i -> i.getProduto().getId().equals(itemPedido.getProduto().getId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemPedido item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + itemPedido.getQuantidade());
        } else {
            Produto produto = produtoService.findById(itemPedido.getProduto().getId());
            itemPedido.setProduto(produto);
            itemPedido.setPrecoUnitario(produto.getPreco());
            itemPedido.setPedido(pedido);
            pedido.getItens().add(itemPedido);
        }

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido updateItemQuantity(Long pedidoId, Long itemId, Integer novaQuantidade) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        ItemPedido item = pedido.getItens().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found in this order"));

        if (novaQuantidade < 0) {
            throw new BadRequestException("Quantity cannot be negative.");
        }

        if (novaQuantidade == 0) {
            pedido.getItens().remove(item);
            itemPedidoRepository.delete(item);
        } else {
            item.setQuantidade(novaQuantidade);
        }

        return pedidoRepository.save(pedido);
    }

    private Pedido transition(Pedido pedido, StatusPedido esperado, StatusPedido novo) {
        validateTransition(pedido, esperado, novo);
        pedido.setStatusPedido(novo);
        Pedido salvo = pedidoRepository.save(pedido);
        notifyKitchen(salvo);
        return salvo;
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido startPreparation(Long pedidoId) {
        Pedido pedido = searchOrder(pedidoId);
        validateOrder(pedido);
        return transition(pedido, StatusPedido.CRIADO, StatusPedido.EM_PREPARO);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido markAsDone(Long pedidoId) {
        Pedido pedido = searchOrder(pedidoId);
        return transition(pedido, StatusPedido.EM_PREPARO, StatusPedido.PRONTO);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido markAsDelivered(Long pedidoId) {
        Pedido pedido = searchOrder(pedidoId);
        return transition(pedido, StatusPedido.PRONTO, StatusPedido.ENTREGUE);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido finish(Long pedidoId) {
        Pedido pedido = searchOrder(pedidoId);
        return transition(pedido, StatusPedido.ENTREGUE, StatusPedido.FINALIZADO);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido cancel(Long pedidoId) {
        Pedido pedido = searchOrder(pedidoId);
        if (pedido.getStatusPedido() == StatusPedido.EM_PREPARO || pedido.getStatusPedido() == StatusPedido.FINALIZADO) {
            throw new BadRequestException("It is not possible to cancel an order that is being prepared or has already been completed.");
        }
        pedido.setStatusPedido(StatusPedido.CANCELADO);
        Pedido salvo = pedidoRepository.save(pedido);
        notifyKitchen(salvo);
        return salvo;
    }

    private void notifyKitchen(Pedido pedido) {
        messagingTemplate.convertAndSend("/topic/cozinha", mapper.toResponse(pedido));
    }

    private Pedido searchOrder(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    private void validateTransition(Pedido pedido, StatusPedido esperado, StatusPedido novo) {
        if (pedido.getStatusPedido() != esperado) {
            throw new BadRequestException(
                    "It is not possible to change from " + pedido.getStatusPedido() + " to " + novo);
        }
    }

    private void validateOrder (Pedido pedido){
        if (pedido.getItens() == null || pedido.getItens().isEmpty()){
            throw new BadRequestException(
                    "it is not possible to send an empty list to the kicthen"
            );
        }
    }
}
