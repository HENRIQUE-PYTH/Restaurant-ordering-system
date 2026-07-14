package com.actuation_system.usuario.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    public UsuarioController(UsuarioService service, UsuarioMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

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
    @ApiResponse
    public UsuarioResponseDTO findByUser (@PathVariable Long userId){
        Usuario user = service.findByUser(userId);
        return mapper.toResponse(user);
    }

    @PostMapping
    public UsuarioResponseDTO createUser (@RequestBody UsuarioRequestDTO dto){
        Usuario user = mapper.toEntity(dto);
        Usuario create = service.createUser(user);
        return mapper.toResponse(create);
    }

    @PutMapping("/{userId}")
    public UsuarioResponseDTO updateUser (@PathVariable Long userId,
                                          @RequestBody UsuarioRequestDTO dto){
        Usuario user = mapper.toEntity(dto);
        Usuario find = service.updateUser(userId, user);
        return mapper.toResponse(find);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser (Long userId){
        service.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
