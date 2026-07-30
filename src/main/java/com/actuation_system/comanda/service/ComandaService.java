package com.actuation_system.comanda.service;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.comanda.mapper.ComandaMapper;
import com.actuation_system.comanda.repository.ComandaRepository;
import com.actuation_system.exceptions.BadRequestException;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.mesa.entity.Mesa;
import com.actuation_system.mesa.repository.MesaRepository;
import com.actuation_system.mesa.service.MesaService;
import com.actuation_system.pedido.StatusPedido;
import com.actuation_system.usuario.entity.Usuario;
import com.actuation_system.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final ComandaMapper mapper;
    private final MesaService mesaService;
    private final UsuarioService usuarioService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MesaRepository mesaRepository;

    public Page<Comanda> findAll (Pageable pageable){
        return comandaRepository.findAll(pageable);
    }

    public Comanda
    searchActiveCommandByTokenMesa(String token) {
        Mesa mesa = mesaRepository.findByQrCodeToken(token)
                .orElseThrow(() -> new NotFoundException("Mesa não encontrada"));

        return comandaRepository.findByMesaAndStatus(mesa, StatusComanda.ABERTA)
                .orElseThrow(() -> new NotFoundException("Nenhuma comanda ativa para essa mesa"));
    }

    public Comanda findById (Long id){
        return comandaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Comanda openTab (Comanda comanda) {
        Mesa mesa = mesaService.findById(comanda.getMesa().getId());
        Usuario usuario = usuarioService.findById(comanda.getUsuario().getId());

        boolean mesaJaTemComandaAberta = comandaRepository.existsByMesaAndStatus(mesa, StatusComanda.ABERTA);
        if (mesaJaTemComandaAberta) {
            throw new BadRequestException("This table already has an open tab.");
        }

        comanda.setMesa(mesa);
        comanda.setUsuario(usuario);
        comanda.setStatus(StatusComanda.ABERTA);
        comanda.setAbertura(LocalDateTime.now());
        Comanda save = comandaRepository.save(comanda);
        notifyTables(save);
        return save;
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Comanda closeTab (Long comandaId) {
        Comanda comanda = findById(comandaId);

        if (comanda.getStatus() != StatusComanda.AGUARDANDO_PAGAMENTO) {
            throw new BadRequestException("The tab must be awaiting payment in order to be closed.");
        }

        boolean temPedidoEmAberto = comanda.getPedidos().stream()
                .anyMatch(p -> p.getStatusPedido() != StatusPedido.FINALIZADO
                        && p.getStatusPedido() != StatusPedido.CANCELADO);

        if (temPedidoEmAberto) {
            throw new BadRequestException("It is not possible to close the tab while orders are still in progress.");
        }

        comanda.setStatus(StatusComanda.FINALIZADA);
        comanda.setFechamento(LocalDateTime.now());
        Comanda save = comandaRepository.save(comanda);
        notifyTables(save);
        return save;
    }


    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Comanda requestPayment(Long comandaId) {
        Comanda comanda = findById(comandaId);

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new BadRequestException("You can only request payment for an open tab.");
        }

        comanda.setStatus(StatusComanda.AGUARDANDO_PAGAMENTO);
        Comanda save = comandaRepository.save(comanda);
        notifyTables(save);
        return save;
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Comanda cancelOrder(Long comandaId) {
        Comanda comanda = findById(comandaId);

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new BadRequestException("It is only possible to cancel an open tab.");
        }

        if (!comanda.getPedidos().isEmpty()) {
            throw new BadRequestException("It is not possible to cancel a tab that already has orders.");
        }

        comanda.setStatus(StatusComanda.CANCELADA);
        Comanda save = comandaRepository.save(comanda);
        notifyTables(save);
        return save;
    }

    private void notifyTables(Comanda comanda) {
        messagingTemplate.convertAndSend("/topic/mesas", mapper.toResponse(comanda));
    }


}
