package com.actuation_system.atendimento.service;

import com.actuation_system.atendimento.StatusAtendimento;
import com.actuation_system.atendimento.entity.Atendimento;
import com.actuation_system.atendimento.repository.AtendimentoRepository;
import com.actuation_system.exceptions.BadRequestException;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.usuario.entity.Usuario;
import com.actuation_system.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final UsuarioService usuarioService;



    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Atendimento aceitar(Long atendimentoId, Long usuarioId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new NotFoundException("Atendimento não encontrado"));

        if (atendimento.getStatus() != StatusAtendimento.AGUARDANDO) {
            throw new BadRequestException("Esse atendimento já foi aceito ou finalizado");
        }

        Usuario usuario = usuarioService.findById(usuarioId);
        atendimento.setUsuario(usuario);
        atendimento.setStatus(StatusAtendimento.EM_ATENDIMENTO);
        return atendimentoRepository.save(atendimento);
    }

    @PreAuthorize("hasAnyRole('GARCOM', 'DONO')")
    public Atendimento finalizar(Long atendimentoId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new NotFoundException("Atendimento não encontrado"));

        if (atendimento.getStatus() != StatusAtendimento.EM_ATENDIMENTO) {
            throw new BadRequestException("Esse atendimento ainda não foi aceito");
        }

        atendimento.setStatus(StatusAtendimento.FINALIZADO);
        return atendimentoRepository.save(atendimento);
    }
}
