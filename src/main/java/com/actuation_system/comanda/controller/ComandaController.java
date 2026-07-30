package com.actuation_system.comanda.controller;

import com.actuation_system.comanda.dto.ComandaRequestDTO;
import com.actuation_system.comanda.dto.ComandaResponseDTO;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.comanda.mapper.ComandaMapper;
import com.actuation_system.comanda.service.ComandaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comandas")
@RequiredArgsConstructor
public class ComandaController {

    private final ComandaService service;
    private final ComandaMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ComandaResponseDTO>> getAll (
            @PageableDefault(size = 20, sort = "abertura", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Comanda> comandas = service.findAll(pageable);
        Page<ComandaResponseDTO> response = comandas.map(mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/qrcode/{codigo}/comanda")
    public ResponseEntity<ComandaResponseDTO> viewOrder(@PathVariable String codigo) {
        Comanda comanda = service.searchActiveCommandByTokenMesa(codigo);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @GetMapping("/{comandaId}")
    public ResponseEntity<ComandaResponseDTO> findById (@PathVariable Long comandaId){
        Comanda comanda = service.findById(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @PostMapping
    public ResponseEntity<ComandaResponseDTO> openTab (@RequestBody @Valid ComandaRequestDTO dto){
        Comanda comanda = mapper.toEntity(dto);
        Comanda open = service.openTab(comanda);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(open));
    }

    @PatchMapping("/{comandaId}/solicitar-pagamento")
    public ResponseEntity<ComandaResponseDTO> requestPayment(@PathVariable Long comandaId) {
        Comanda comanda = service.requestPayment(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @PatchMapping("/{comandaId}/fechar")
    public ResponseEntity<ComandaResponseDTO> closeTab(@PathVariable Long comandaId) {
        Comanda comanda = service.closeTab(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @PatchMapping("/{comandaId}/cancelar")
    public ResponseEntity<ComandaResponseDTO> cancelOrder(@PathVariable Long comandaId) {
        Comanda comanda = service.cancelOrder(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

}
