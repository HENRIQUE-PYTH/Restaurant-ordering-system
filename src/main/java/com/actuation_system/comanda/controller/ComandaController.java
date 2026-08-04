package com.actuation_system.comanda.controller;

import com.actuation_system.comanda.dto.ComandaRequestDTO;
import com.actuation_system.comanda.dto.ComandaResponseDTO;
import com.actuation_system.comanda.dto.ComandaResumoDTO;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.comanda.mapper.ComandaMapper;
import com.actuation_system.comanda.service.ComandaService;
import com.actuation_system.exceptions.ErrorResponse;
import com.actuation_system.mesa.entity.Mesa;
import com.actuation_system.usuario.entity.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comandas")
@RequiredArgsConstructor
public class ComandaController {

    private final ComandaService service;
    private final ComandaMapper mapper;

    @GetMapping
    @Operation(
            summary = "Returns a paginated list of tabs",
            description = "Retrieves all tabs using pagination and sorting."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tabs retrieved successfully.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ComandaResumoDTO.class))
            )
    )
    public ResponseEntity<Page<ComandaResumoDTO>> getAll(
            @PageableDefault(size = 20, sort = "abertura", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Comanda> comandas = service.findAll(pageable);
        Page<ComandaResumoDTO> response = comandas.map(mapper::toResumo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/qrcode/{codigo}/comanda")
    @Operation(
            summary = "Find an active tab by QR Code",
            description = "Retrieves the active tab associated with the specified table QR Code token."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Active tab found successfully.",
            content = @Content(schema = @Schema(implementation = ComandaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "No active tab found for the provided QR Code.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ComandaResponseDTO> viewOrder(@PathVariable String codigo) {
        Comanda comanda = service.searchActiveCommandByTokenMesa(codigo);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @GetMapping("/{comandaId}")
    @Operation(
            summary = "Find a tab by ID",
            description = "Retrieves a tab along with all its associated orders."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tab found successfully.",
            content = @Content(schema = @Schema(implementation = ComandaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Tab not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ComandaResponseDTO> findByIdWithOrder(@PathVariable Long comandaId) {
        Comanda comanda = service.findByIdComPedidos(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @PostMapping
    @Operation(
            summary = "Open a new tab",
            description = "Creates a new tab for a table and assigns it to a waiter."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Tab opened successfully.",
            content = @Content(schema = @Schema(implementation = ComandaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "The table already has an open tab or the request is invalid.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
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

    @PatchMapping("/{comandaId}/solicitar-pagamento")
    @Operation(
            summary = "Request payment",
            description = "Changes the tab status from OPEN to WAITING_FOR_PAYMENT."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Payment requested successfully.",
            content = @Content(schema = @Schema(implementation = ComandaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "The tab is not eligible for payment request.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ComandaResponseDTO> requestPayment(@PathVariable Long comandaId) {
        Comanda comanda = service.requestPayment(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @PatchMapping("/{comandaId}/fechar")
    @Operation(
            summary = "Close a tab",
            description = "Finalizes the tab after confirming that all orders have been completed."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tab closed successfully.",
            content = @Content(schema = @Schema(implementation = ComandaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "The tab cannot be closed because it is not awaiting payment or there are unfinished orders.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ComandaResponseDTO> closeTab(@PathVariable Long comandaId) {
        Comanda comanda = service.closeTab(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

    @PatchMapping("/{comandaId}/cancelar")
    @Operation(
            summary = "Cancel a tab",
            description = "Cancels an open tab that is still eligible for cancellation."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tab canceled successfully.",
            content = @Content(schema = @Schema(implementation = ComandaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "The tab cannot be canceled.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Tab not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ComandaResponseDTO> cancelOrder(@PathVariable Long comandaId) {
        Comanda comanda = service.cancelOrder(comandaId);
        return ResponseEntity.ok(mapper.toResponse(comanda));
    }

}
