package com.actuation_system.mesa.repository;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.mesa.StatusMesa;
import com.actuation_system.mesa.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByStatus (StatusMesa statusMesa);
    List<Mesa> findTableWithActiveOrder (Long id, StatusComanda statusComanda);
    Optional<Mesa> findByIdAndComandasStatus(Long id, StatusComanda status);
}
