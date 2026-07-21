package com.actuation_system.categoria.controller;

import com.actuation_system.categoria.dto.CategoriaRequestDTO;
import com.actuation_system.categoria.dto.CategoriaResponseDTO;
import com.actuation_system.categoria.entity.Categoria;
import com.actuation_system.categoria.mapper.CategoriaMapper;
import com.actuation_system.categoria.service.CategoriaService;
import com.actuation_system.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaMapper mapper;
    private final CategoriaService service;


    @GetMapping
    @Operation(
            summary = "Return a complete list of the restaurant's categories.",
            description = "Return all restaurant categories."
    )
    @ApiResponse(
            responseCode = "200",
            description = "General list returned.",
            content = @Content (schema = @Schema(implementation = CategoriaResponseDTO.class))
    )
    public List<CategoriaResponseDTO> getAllCategories (){
        return service.getAllCategories()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{categoryId}")
    @Operation(
            summary = "Return one category by ID.",
            description = "Searche for category by their specific ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Category found",
            content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Category not found by their ID.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<CategoriaResponseDTO> findByCategory (@PathVariable Long categoryId){
        Categoria find = service.findByCategory(categoryId);
        return ResponseEntity.ok(mapper.toResponse(find));
    }

    @PostMapping
    @Operation(
            summary = "Create one category.",
            description = "A new category is created and added to the menu."
    )
    @ApiResponse(
            responseCode = "201",
            description = "One new category is a created.",
            content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Inválid data for the create has been new category",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<CategoriaResponseDTO> createCategories (@RequestBody @Valid CategoriaRequestDTO dto){
        Categoria categoria = mapper.toEntity(dto);
        Categoria create = service.createCategory(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(create));
    }

    @PutMapping("/{categoryId}/update")
    @Operation(
            summary = "Update one category by specific ID.",
            description = "A specific category can be modified by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Category not found.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Inválid data for update a category.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CategoriaResponseDTO> updateCategory (@PathVariable Long categoryId,
                                                @RequestBody @Valid CategoriaRequestDTO dto){
        Categoria categoria = mapper.toEntity(dto);
        Categoria update = service.updateCategory(categoryId, categoria);
        return ResponseEntity.ok(mapper.toResponse(update));

    }

    @DeleteMapping("/{categoryId}/delete")
    @Operation(
            summary = "delete the category by ID.",
            description = "delete the existing category by ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Category excluded on sucessfully."
    )

    @ApiResponse(
            responseCode = "404",
            description = "Category not found for the ID.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Void> deleteCategory (@PathVariable Long categoryId){
        service.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}
