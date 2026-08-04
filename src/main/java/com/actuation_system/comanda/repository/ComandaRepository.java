package com.actuation_system.comanda.repository;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.mesa.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    boolean existsByMesaAndStatus(Mesa mesa, StatusComanda status);

    @Query("SELECT c FROM Comanda c LEFT JOIN FETCH c.pedidos WHERE c.mesa = :mesa AND c.status = :status")
    Optional<Comanda> findByMesaAndStatusComPedidos(@Param("mesa") Mesa mesa, @Param("status") StatusComanda status);

    @Query("SELECT c FROM Comanda c LEFT JOIN FETCH c.pedidos WHERE c.id = :id")
    Optional<Comanda> findByIdComPedidos(@Param("id") Long id);
}
