package com.actuation_system.pedido.service;

import com.actuation_system.exceptions.BadRequestException;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.pedido.StatusPedido;
import com.actuation_system.pedido.entity.ItemPedido;
import com.actuation_system.pedido.entity.Pedido;
import com.actuation_system.pedido.repositorio.PedidoRepository;
import com.actuation_system.produto.entity.Produto;
import com.actuation_system.produto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;

    public Page<Pedido> getAll (Pageable pageable){
        return pedidoRepository.findAll(pageable);
    }

    public Pedido findById (Long id){
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public Pedido createOrder (Pedido pedido){

        Pedido order = new Pedido();
        order.setComanda(pedido.getComanda());
        order.setHorario(pedido.getHorario());
        order.setItens(pedido.getItens());
        order.setStatusPedido(StatusPedido.CRIADO);

        return pedidoRepository.save(order);
    }

    public Pedido adicionarItem(Long pedidoId, ItemPedido itemPedido) {
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

    public Pedido alterarQuantidadeItem(Long pedidoId, Long itemId, Integer novaQuantidade) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        ItemPedido item = pedido.getItens().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found in this order"));

        if (novaQuantidade <= 0) {
            pedido.getItens().remove(item);
        } else {
            item.setQuantidade(novaQuantidade);
        }

        return pedidoRepository.save(pedido);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido iniciarPreparo(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        validarTransicao(pedido, StatusPedido.CRIADO, StatusPedido.EM_PREPARO);
        pedido.setStatusPedido(StatusPedido.EM_PREPARO);
        return pedidoRepository.save(pedido);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido marcarPronto(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        validarTransicao(pedido, StatusPedido.EM_PREPARO, StatusPedido.PRONTO);
        pedido.setStatusPedido(StatusPedido.PRONTO);
        return pedidoRepository.save(pedido);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido marcarEntregue(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        validarTransicao(pedido, StatusPedido.PRONTO, StatusPedido.ENTREGUE);
        pedido.setStatusPedido(StatusPedido.ENTREGUE);
        return pedidoRepository.save(pedido);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido finalizar(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        validarTransicao(pedido, StatusPedido.ENTREGUE, StatusPedido.FINALIZADO);
        pedido.setStatusPedido(StatusPedido.FINALIZADO);
        return pedidoRepository.save(pedido);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Pedido cancelar(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);
        if (pedido.getStatusPedido() == StatusPedido.EM_PREPARO || pedido.getStatusPedido() == StatusPedido.FINALIZADO) {
            throw new BadRequestException("It is not possible to cancel an order that is being prepared or has already been completed.");
        }
        pedido.setStatusPedido(StatusPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }


    private void validarTransicao(Pedido pedido, StatusPedido esperado, StatusPedido novo) {
        if (pedido.getStatusPedido() != esperado) {
            throw new BadRequestException("It is not possible to change from " + pedido.getStatusPedido() + " to " + novo);
        }
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found."));
    }
}
