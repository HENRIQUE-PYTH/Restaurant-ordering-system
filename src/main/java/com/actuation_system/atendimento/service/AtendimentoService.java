package com.actuation_system.atendimento.service;

import com.actuation_system.atendimento.StatusAtendimento;
import com.actuation_system.atendimento.entity.Atendimento;
import com.actuation_system.atendimento.mapper.AtendimentoMapper;
import com.actuation_system.atendimento.repository.AtendimentoRepository;
import com.actuation_system.exceptions.BadRequestException;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.mesa.entity.Mesa;
import com.actuation_system.mesa.repository.MesaRepository;
import com.actuation_system.usuario.entity.Usuario;
import com.actuation_system.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UsuarioService usuarioService;
    private final MesaRepository mesaRepository;
    private final AtendimentoMapper mapper;

    public List<Atendimento> getAll(){
        return atendimentoRepository.findAll();
    }

    public Atendimento findById (Long id){
        return atendimentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("service not found"));
    }

    public Atendimento create(Atendimento atendimento) {
        Mesa mesa = mesaRepository.findByQrCodeToken(atendimento.getMesa().getQrCodeToken())
                .orElseThrow(() -> new NotFoundException("Mesa não encontrada"));

        atendimento.setMesa(mesa);
        atendimento.setStatus(StatusAtendimento.AGUARDANDO);
        atendimento.setHorario(LocalDateTime.now());
        Atendimento salvo = atendimentoRepository.save(atendimento);
        notifyService(salvo);
        return salvo;
    }


    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Atendimento accept (Long atendimentoId, Long usuarioId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new NotFoundException("Service request not found"));

        if (atendimento.getStatus() != StatusAtendimento.AGUARDANDO) {
            throw new BadRequestException("This service request has already been accepted or completed.");
        }

        Usuario usuario = usuarioService.findById(usuarioId);
        atendimento.setUsuario(usuario);
        atendimento.setStatus(StatusAtendimento.EM_ATENDIMENTO);
        Atendimento save = atendimentoRepository.save(atendimento);
        notifyService(save);
        return save;
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Atendimento finish(Long atendimentoId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new NotFoundException("Service request not found"));

        if (atendimento.getStatus() != StatusAtendimento.EM_ATENDIMENTO) {
            throw new BadRequestException("This service request has not yet been accepted.");
        }

        atendimento.setStatus(StatusAtendimento.FINALIZADO);
        return atendimentoRepository.save(atendimento);
    }

    private void notifyService(Atendimento atendimento) {
        messagingTemplate.convertAndSend("/topic/atendimentos", mapper.toResponse(atendimento));
    }
}
