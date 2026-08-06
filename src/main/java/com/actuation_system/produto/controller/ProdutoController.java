package com.actuation_system.produto.controller;

import com.actuation_system.exceptions.ErrorResponse;
import com.actuation_system.produto.dto.ProdutoRequestDTO;
import com.actuation_system.produto.dto.ProdutoResponseDTO;
import com.actuation_system.produto.entity.Produto;
import com.actuation_system.produto.mapper.ProdutoMapper;
import com.actuation_system.produto.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;
    private final ProdutoMapper mapper;

    @GetMapping
    @Operation(
            summary = "Returns all products in a paginated format.",
            description = "All requests return paginated results instead of a single list."
    )
    @ApiResponse(
            responseCode = "200",
            description = "General pagination returned.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ProdutoResponseDTO.class)))
    )
    public ResponseEntity<Page<ProdutoResponseDTO>> getAllProduct(
            @PageableDefault (size = 20, sort = "name")Pageable pageable){
        Page<Produto> produtos = service.getAllProduct(pageable);
        Page<ProdutoResponseDTO> response = produtos.map(mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{produtoId}")
    @Operation(
            summary = "Return a specific product by Id.",
            description = "A product specific is returned for by Id."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Product found.",
            content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Product not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ProdutoResponseDTO> findById (@PathVariable Long produtoId){
        Produto produto = service.findById(produtoId);
        return ResponseEntity.ok(mapper.toResponse(produto));
    }

    @PostMapping
    @Operation(
            summary = "Creates a new product.",
            description = "Create a new product so you can place it in a specific category. "
    )
    @ApiResponse(
            responseCode = "201",
            description = "A new product has created.",
            content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Inválid data for creation of a new product.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ProdutoResponseDTO> createProduto(@RequestBody @Valid ProdutoRequestDTO dto){
        Produto produto = mapper.toEntity(dto);
        Produto save = service.createProduct(produto, dto.categoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(save));
    }

    @PutMapping("/{produtoId}/{categoryId}/update")
    @Operation(
            summary = "The product can be update.",
            description = "It is possible to update or change the price, name, or category of this product."
    )
    @ApiResponse(
            responseCode = "200",
            description = "The product has been updating.",
            content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Inválid data for updating product.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "There is already another product registered with this name.",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProdutoResponseDTO> updateProduct (
            @RequestBody @Valid ProdutoRequestDTO dto,
            @PathVariable Long produtoId,
            @PathVariable Long categoryId){
        Produto produto = mapper.toEntity(dto);
        Produto update = service.updateProduct(produtoId,categoryId, produto);

        return ResponseEntity.ok(mapper.toResponse(update));
    }

    @DeleteMapping("/{produtoId}")
    @Operation(
            summary = "The product can be deleted.",
            description = "You can delete a specific product by Id."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Product excluded on sucessfully."
    )
    @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<Void> delete (@PathVariable Long produtoId){
        service.deleteProduct(produtoId);
        return ResponseEntity.noContent().build();
    }


}
