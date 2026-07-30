package com.actuation_system.comanda.repository;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.mesa.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {
    boolean existsByMesaAndStatus(Mesa mesa, StatusComanda status);
    Optional<Comanda> findByMesaAndStatus(Mesa mesa, StatusComanda status);
}
