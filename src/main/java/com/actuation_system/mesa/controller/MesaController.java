package com.actuation_system.mesa.controller;

import com.actuation_system.exceptions.ErrorResponse;
import com.actuation_system.mesa.dto.MesaRequestDTO;
import com.actuation_system.mesa.dto.MesaResponseDTO;
import com.actuation_system.mesa.entity.Mesa;
import com.actuation_system.mesa.mapper.MesaMapper;
import com.actuation_system.mesa.service.MesaService;
import com.actuation_system.mesa.service.QrCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService service;
    private final MesaMapper mapper;
    private final QrCodeService qrCodeService;

    @GetMapping
    @Operation(
            summary = "Return a list of tables.",
            description = "Return a general list of tables occuped or not."
    )
    @ApiResponse(
            responseCode = "200",
            description = "general list of the table returned",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = MesaResponseDTO.class)))
    )
    public List<MesaResponseDTO> getAllTables(){
        return service.getAllTables()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping(value = "/{mesaId}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(
            summary = "Generate a table QR Code",
            description = "Generates and returns the QR Code image associated with the specified table."
    )
    @ApiResponse(
            responseCode = "200",
            description = "QR Code generated successfully.",
            content = @Content(
                    mediaType = MediaType.IMAGE_PNG_VALUE,
                    schema = @Schema(type = "string", format = "binary")
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Table not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<byte[]> getQrCodeImage(@PathVariable Long mesaId) {
        Mesa mesa = service.findById(mesaId);
        byte[] imagem = qrCodeService.gerarImagemQrCode(mesa.getQrCodeToken());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imagem);
    }

    @GetMapping("/available")
    @Operation(
            summary = "Returns a list of available tables.",
            description = "Returns a general list of all available tables."
    )
    @ApiResponse(
            responseCode = "200",
            description = "A general list of the table available returned.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = MesaResponseDTO.class)))
    )
    public List<MesaResponseDTO> searchAvailableTables (){
        return service.searchAvailableTables()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/occupied")
    @Operation(
            summary = "Returns a list of occupied tables.",
            description = "A list of all occupied tables is returned."
    )
    @ApiResponse(
            responseCode = "200",
            description = "A general list of the tables occupied was returned.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = MesaResponseDTO.class)))
    )
    public List<MesaResponseDTO> findOccupiedTables (){
        return service.findOccupiedTables()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/closing")
    @Operation(
            summary = "Returns a list of tables that are closing out their checks.",
            description = "Tables that are settling their bills are returned to the system."
    )
    @ApiResponse(
            responseCode = "200",
            description = "A general list of all tables currently closing out their checks was returned.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = MesaResponseDTO.class)))
    )
    public List<MesaResponseDTO> findClosingTables (){
        return service.findClosingTables()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/has-active-order/{tableId}")
    @Operation(
            summary = "Verify if a table have a order active.",
            description = "The waiter can check if a table has an active order."
    )
    @ApiResponse(
            responseCode = "200",
            description = "The table has found.",
            content = @Content(schema = @Schema(implementation = MesaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "The table is not found by can Id.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Boolean> hasActiveOrder(@PathVariable Long tableId) {
        boolean temComandaAtiva = service.hasActiveOrder(tableId);
        return ResponseEntity.ok(temComandaAtiva);
    }

    @GetMapping("/qrcode/{token}")
    @Operation(
            summary = "searches for a table by its token.",
            description = "Dedicated endpoint only for teste can be developers."
    )
    @ApiResponse(
            responseCode = "200",
            description = "The table has found.",
            content = @Content(schema = @Schema(implementation = MesaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "The table is not found by can Id.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<MesaResponseDTO> findByQrCode(@PathVariable String token) {
        Mesa mesa = service.findByQrCodeToken(token);
        return ResponseEntity.ok(mapper.toResponse(mesa));
    }

    @GetMapping("/{tableId}")
    @Operation(
            summary = "A table with a specific ID is returned.",
            description = "The waiter can find a table by specific Id."
    )
    @ApiResponse(
            responseCode = "200",
            description = "The table was found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = MesaResponseDTO.class)))
    )
    @ApiResponse(
            responseCode = "404",
            description = "The table is not found by can Id"
    )
    public ResponseEntity<MesaResponseDTO> findById (@PathVariable Long tableId){
        Mesa table = service.findById(tableId);
        return ResponseEntity.ok(mapper.toResponse(table));
    }

    @PostMapping
    @Operation(
            summary = "Create a new user and adds for the system.",
            description = "When a new user is created, they are added to the system with the role of waiter. "
    )
    @ApiResponse(
            responseCode = "201",
            description = "Table creating with sucessfuly",
            content = @Content(schema = @Schema(implementation = MesaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "inválid data for creating the table.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<MesaResponseDTO> createTable (@RequestBody @Valid MesaRequestDTO dto){
        Mesa mesa = mapper.toEntity(dto);
        Mesa save = service.createTable(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(save));
    }


}
