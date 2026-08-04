package com.actuation_system.atendimento.controller;

import com.actuation_system.atendimento.dto.AtendimentoRequestDTO;
import com.actuation_system.atendimento.dto.AtendimentoResponseDTO;
import com.actuation_system.atendimento.entity.Atendimento;
import com.actuation_system.atendimento.mapper.AtendimentoMapper;
import com.actuation_system.atendimento.service.AtendimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/atendimentos")
public class AtendimentoController {

    private final AtendimentoService service;
    private final AtendimentoMapper mapper;

    @GetMapping
    public List<AtendimentoResponseDTO> getAll (){
        return service.getAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{atendimentoId}")
    public ResponseEntity<AtendimentoResponseDTO> findById (@PathVariable Long atendimentoId){
        Atendimento atendimento = service.findById(atendimentoId);
        return ResponseEntity.ok(mapper.toResponse(atendimento));

    }

    @PostMapping
    public ResponseEntity<AtendimentoResponseDTO> createService (@RequestBody AtendimentoRequestDTO dto){

        Atendimento entity = mapper.toEntity(dto);
        Atendimento atendimento = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(atendimento));
    }

    @PatchMapping("/{atendimentoId}/aceitar")
    public ResponseEntity<AtendimentoResponseDTO> accept (
            @PathVariable Long atendimentoId,
            @PathVariable Long usuarioId){
        Atendimento atendimento = service.accept(atendimentoId, usuarioId);
        return ResponseEntity.ok(mapper.toResponse(atendimento));
    }

    @PatchMapping("/{atendimentoId}/finalizar")
    public ResponseEntity<AtendimentoResponseDTO> finish (@PathVariable Long atendimentoId){
        Atendimento atendimento = service.finish(atendimentoId);
        return ResponseEntity.ok(mapper.toResponse(atendimento));
    }
}
