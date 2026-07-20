package com.actuation_system.produto.repository;

import com.actuation_system.categoria.entity.Categoria;
import com.actuation_system.produto.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByNameAndCategoria (String name, Categoria categoria);
}
