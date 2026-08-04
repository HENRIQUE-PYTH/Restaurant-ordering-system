package com.actuation_system.atendimento.controller;

import com.actuation_system.atendimento.dto.AtendimentoRequestDTO;
import com.actuation_system.atendimento.dto.AtendimentoResponseDTO;
import com.actuation_system.atendimento.entity.Atendimento;
import com.actuation_system.atendimento.mapper.AtendimentoMapper;
import com.actuation_system.atendimento.service.AtendimentoService;
import com.actuation_system.exceptions.ErrorResponse;
import com.actuation_system.mesa.entity.Mesa;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Operation(
            summary = "Returns all service requests",
            description = "Retrieves all customer service requests."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Service requests retrieved successfully.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = AtendimentoResponseDTO.class))
            )
    )
    public List<AtendimentoResponseDTO> getAll (){
        return service.getAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{atendimentoId}")
    @Operation(
            summary = "Find a service request by ID",
            description = "Retrieves a customer service request using its unique identifier."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Service request found successfully.",
            content = @Content(schema = @Schema(implementation = AtendimentoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Service request not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<AtendimentoResponseDTO> findById (@PathVariable Long atendimentoId){
        Atendimento atendimento = service.findById(atendimentoId);
        return ResponseEntity.ok(mapper.toResponse(atendimento));

    }

    @PostMapping
    @Operation(
            summary = "Create a new service request",
            description = "Creates a new customer service request for a table using its QR Code token."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Service request created successfully.",
            content = @Content(schema = @Schema(implementation = AtendimentoResponseDTO.class))
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Table not found.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<AtendimentoResponseDTO> createService(@Valid @RequestBody AtendimentoRequestDTO dto) {
        Atendimento atendimento = mapper.toEntity(dto);

        Mesa mesaRef = new Mesa();
        mesaRef.setQrCodeToken(dto.qrCodeToken());
        atendimento.setMesa(mesaRef); // ← esse passo que provavelmente falta

        Atendimento salvo = service.create(atendimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salvo));
    }

    @PatchMapping("/{atendimentoId}/{usuarioId}/aceitar")
    @Operation(
            summary = "Accept a service request",
            description = "Assigns a waiter to an existing customer service request."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Service request accepted successfully.",
            content = @Content(schema = @Schema(implementation = AtendimentoResponseDTO.class))
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "400",
                    description = "The service request cannot be accepted.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Service request or waiter not found.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<AtendimentoResponseDTO> accept (
            @PathVariable Long atendimentoId,
            @PathVariable Long usuarioId){
        Atendimento atendimento = service.accept(atendimentoId, usuarioId);
        return ResponseEntity.ok(mapper.toResponse(atendimento));
    }

    @PatchMapping("/{atendimentoId}/finalizar")
    @Operation(
            summary = "Finish a service request",
            description = "Marks a customer service request as completed."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Service request completed successfully.",
            content = @Content(schema = @Schema(implementation = AtendimentoResponseDTO.class))
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "400",
                    description = "The service request cannot be completed.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Service request not found.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<AtendimentoResponseDTO> finish (@PathVariable Long atendimentoId){
        Atendimento atendimento = service.finish(atendimentoId);
        return ResponseEntity.ok(mapper.toResponse(atendimento));
    }
}
