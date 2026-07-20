package com.actuation_system.produto.service;

import com.actuation_system.categoria.entity.Categoria;
import com.actuation_system.categoria.service.CategoriaService;
import com.actuation_system.exceptions.BadRequestException;
import com.actuation_system.exceptions.ConflictRequestException;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.produto.entity.Produto;
import com.actuation_system.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;

    public Page<Produto> getAllProduct (Pageable pageable){
        return produtoRepository.findAll(pageable);
    }

    public Produto findById (Long id){
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found by ID"));
    }

    @PreAuthorize("hasRole('DONO')")
    public Produto createProduct (Produto produto, Long categoryId){

        Categoria categoria = categoriaService.findByCategory(categoryId);

        if (produto.getPreco().compareTo(BigDecimal.ZERO) <= 0){
            throw new BadRequestException("The price must be positive.");
        }

        Produto save = new Produto();

        if (produtoRepository.existsByNameAndCategoria(produto.getName(), categoria)) {
            throw new ConflictRequestException("Já existe um produto com esse nome nessa categoria");
        }

        save.setName(produto.getName());
        save.setPreco(produto.getPreco());
        save.setCategoria(categoria);

        try {
            return produtoRepository.save(save);
        }
        catch (DataIntegrityViolationException e){
            throw new ConflictRequestException("Product already cadaster." + e);
        }
    }

    @PreAuthorize("hasRole('DONO')")
    public Produto updateProduct (Long produtoId, Long categoryId, Produto produto){

        Produto find = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new NotFoundException("Product not found by id"));
        Categoria categoria = categoriaService.findByCategory(categoryId);

        if (produto.getPreco().compareTo(BigDecimal.ZERO) < 0){
            throw new BadRequestException("The price must be positive.");
        }

        find.setName(produto.getName());
        find.setPreco(produto.getPreco());
        find.setCategoria(categoria);

        try {
            return produtoRepository.save(find);
        }
        catch (DataIntegrityViolationException e){
            throw new ConflictRequestException("Conflicting data " + e);
        }

    }

    @PreAuthorize("hasRole('DONO')")
    public void deleteProduct (Long produtoId){
        Produto produto = produtoRepository.findById(produtoId)
                        .orElseThrow(() -> new NotFoundException("Product not found by Id"));
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

}
