package com.actuation_system.produto.controller;

import com.actuation_system.produto.dto.ProdutoRequestDTO;
import com.actuation_system.produto.dto.ProdutoResponseDTO;
import com.actuation_system.produto.entity.Produto;
import com.actuation_system.produto.mapper.ProdutoMapper;
import com.actuation_system.produto.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;
    private final ProdutoMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> getAllProduct(
            @PageableDefault (size = 20, sort = "name")Pageable pageable){
        Page<Produto> produtos = service.getAllProduct(pageable);
        Page<ProdutoResponseDTO> response = produtos.map(mapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponseDTO> findById (@PathVariable Long produtoId){
        Produto produto = service.findById(produtoId);
        return ResponseEntity.ok(mapper.toResponse(produto));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> createProduto(@RequestBody @Valid ProdutoRequestDTO dto){

        Produto produto = mapper.toEntity(dto);
        Produto save = service.createProduct(produto, dto.categoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(save));
    }

    @PostMapping("/{produtoId}/{categoryId}/update")
    public ResponseEntity<ProdutoResponseDTO> updateProduct (
            @RequestBody @Valid ProdutoRequestDTO dto,
            @PathVariable Long produtoId,
            @PathVariable Long categoryId){
        Produto produto = mapper.toEntity(dto);
        Produto update = service.updateProduct(produtoId,categoryId, produto);

        return ResponseEntity.ok(mapper.toResponse(update));
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> delete (@PathVariable Long produtoId){
        service.deleteProduct(produtoId);
        return ResponseEntity.noContent().build();
    }


}
