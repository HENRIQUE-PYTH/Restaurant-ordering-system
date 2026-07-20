package com.actuation_system.categoria.service;

import com.actuation_system.categoria.entity.Categoria;
import com.actuation_system.categoria.repository.CategoriaRepository;
import com.actuation_system.exceptions.BadRequestException;
import com.actuation_system.exceptions.ConflictRequestException;
import com.actuation_system.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public List<Categoria> getAllCategories (){
        return repository.findAll();
    }

    public Categoria findByCategory (Long id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @PreAuthorize("hasRole('DONO')")
    public Categoria createCategory(Categoria categoria) {

        if (repository.existsByNomeIgnoreCase(categoria.getNome())) {
            throw new BadRequestException("That category already exist.");
        }

        String nomeFormatado = formatarNome(categoria.getNome());

        Categoria newCategory = new Categoria();
        newCategory.setNome(nomeFormatado);

        try {
            return repository.save(newCategory);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictRequestException("Dados Conflitantes ", e);
        }
    }

    @PreAuthorize("hasRole('DONO')")
    public Categoria updateCategory (Long id, Categoria categoria){
        Categoria find = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found."));

        String nomeFormatado = formatarNome(categoria.getNome());

        find.setNome(nomeFormatado);

        return repository.save(find);
    }

    @PreAuthorize("hasRole('DONO')")
    public void deleteCategory (Long id){
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        repository.delete(categoria);
    }





    private String formatarNome(String nome) {
        String limpo = nome.trim().toLowerCase();
        return limpo.substring(0, 1).toUpperCase() + limpo.substring(1);
    }
}
