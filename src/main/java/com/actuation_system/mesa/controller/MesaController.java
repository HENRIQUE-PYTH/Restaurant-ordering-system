package com.actuation_system.mesa.controller;

import com.actuation_system.exceptions.ErrorResponse;
import com.actuation_system.mesa.dto.MesaRequestDTO;
import com.actuation_system.mesa.dto.MesaResponseDTO;
import com.actuation_system.mesa.entity.Mesa;
import com.actuation_system.mesa.mapper.MesaMapper;
import com.actuation_system.mesa.service.MesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService service;
    private final MesaMapper mapper;

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
    public Optional<MesaResponseDTO> hasActiveOrder(@PathVariable Long tableId){
        return service.hasActiveOrder(tableId)
                .map(mapper::toResponse);
    }

    @GetMapping("/qrcode/{token}")
    public MesaResponseDTO findByQrCode(@PathVariable String token) {

        Mesa mesa = service.findByQrCodeToken(token);

        return mapper.toResponse(mesa);
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
    public MesaResponseDTO findById (@PathVariable Long tableId){
        Mesa table = service.findById(tableId);
        return mapper.toResponse(table);
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
    public MesaResponseDTO createTable (@RequestBody @Valid MesaRequestDTO dto){
        Mesa mesa = mapper.toEntity(dto);
        Mesa save = service.createTable(mesa);
        return mapper.toResponse(save);
    }


}
