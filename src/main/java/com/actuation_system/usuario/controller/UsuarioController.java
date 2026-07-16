package com.actuation_system.usuario.controller;

import com.actuation_system.exceptions.ErrorResponse;
import com.actuation_system.usuario.dto.UsuarioRequestDTO;
import com.actuation_system.usuario.dto.UsuarioResponseDTO;
import com.actuation_system.usuario.entity.Usuario;
import com.actuation_system.usuario.mapper.UsuarioMapper;
import com.actuation_system.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @GetMapping
    @Operation(
            summary = "looking for waiters and the owners",
            description = "return a general list of waiters and the owners"
    )
    @ApiResponse(
            responseCode = "200",
            description = "general list returned",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))
    )
    public List<UsuarioResponseDTO> AllUsers (){
        return service.getAllUsers()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "searches for the waiters and owners by ID",
            description = "searche for waiters and owners by their specific ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "waiters or owners found",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class)
            ))
    @ApiResponse(
            responseCode = "404",
            description = "User not found for by ID",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public UsuarioResponseDTO findByUser (@PathVariable Long userId){
        Usuario user = service.findByUser(userId);
        return mapper.toResponse(user);
    }

    @PostMapping
    @Operation(
            summary = "endpoint for creation a new user",
            description = "dedicated endpoint for registering a new waiter or owner"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User creating with sucessfuly",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "inválid data for creating the user",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public UsuarioResponseDTO createUser (@RequestBody @Valid UsuarioRequestDTO dto){
        Usuario user = mapper.toEntity(dto);
        Usuario create = service.createUser(user);
        return mapper.toResponse(create);
    }

    @PutMapping("/{userId}")
    @Operation(
            summary = "endpoint for update a user",
            description = "dedicated endpoint for updating specific dating of the user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "waiter or owner updated",
            content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "The user not is found.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "The data entered is invalid, please try again with dated correct.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "The email address you entered already exists.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public UsuarioResponseDTO updateUser (@PathVariable Long userId,
                                          @RequestBody @Valid UsuarioRequestDTO dto){
        Usuario user = mapper.toEntity(dto);
        Usuario find = service.updateUser(userId, user);
        return mapper.toResponse(find);
    }

    @DeleteMapping("/{userId}")
    @Operation(
            summary = "delete the user by ID.",
            description = "delete the existing user by ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "User excluded on sucessfully."
    )

    @ApiResponse(
            responseCode = "404",
            description = "User not found for the ID.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Void> deleteUser (@PathVariable Long userId){
        service.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
