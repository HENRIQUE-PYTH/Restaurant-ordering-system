package com.actuation_system.comanda.controller;

import com.actuation_system.comanda.dto.ComandaRequestDTO;
import com.actuation_system.comanda.dto.ComandaResponseDTO;
import com.actuation_system.comanda.dto.ComandaResumoDTO;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.comanda.mapper.ComandaMapper;
import com.actuation_system.comanda.service.ComandaService;
import com.actuation_system.mesa.entity.Mesa;
import com.actuation_system.usuario.entity.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comandas")
@RequiredArgsConstructor
public class ComandaController {

    private final ComandaService service;
    private final ComandaMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ComandaResumoDTO>> getAll(
            @PageableDefault(size = 20, sort = "abertura", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comanda> comandas = service.findAll(pageable);
        Page<ComandaResumoDTO> response = comandas.map(mapper::toResumo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/qrcode/{codigo}/comanda")
    public ResponseEntity<ComandaResponseDTO> viewOrder(@PathVariable String codigo) {
        Comanda comanda = service.searchActiveCommandByTokenMesa(codigo);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @GetMapping("/{comandaId}")
    public ResponseEntity<ComandaResponseDTO> findByIdWithOrder(@PathVariable Long comandaId) {
        Comanda comanda = service.findByIdComPedidos(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @PostMapping
    public ResponseEntity<ComandaResponseDTO> openTab (@RequestBody @Valid ComandaRequestDTO dto){
        Comanda comanda = mapper.toEntity(dto); // aqui, comanda.getMesa() já vem null, de propósito

        Mesa mesaRef = new Mesa();
        mesaRef.setId(dto.mesaId());
        comanda.setMesa(mesaRef); // ← esse passo provavelmente ficou faltando

        Usuario usuarioRef = new Usuario();
        usuarioRef.setId(dto.usuarioId());
        comanda.setUsuario(usuarioRef); // ← e esse também, mesmo motivo

        Comanda salva = service.openTab(comanda);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salva));
    }

    @Transactional
    @PatchMapping("/{comandaId}/solicitar-pagamento")
    public ResponseEntity<ComandaResponseDTO> requestPayment(@PathVariable Long comandaId) {
        Comanda comanda = service.requestPayment(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @Transactional
    @PatchMapping("/{comandaId}/fechar")
    public ResponseEntity<ComandaResponseDTO> closeTab(@PathVariable Long comandaId) {
        Comanda comanda = service.closeTab(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @Transactional
    @PatchMapping("/{comandaId}/cancelar")
    public ResponseEntity<ComandaResponseDTO> cancelOrder(@PathVariable Long comandaId) {
        Comanda comanda = service.cancelOrder(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

}
