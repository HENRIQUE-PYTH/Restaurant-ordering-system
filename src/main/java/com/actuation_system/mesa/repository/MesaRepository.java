package com.actuation_system.mesa.repository;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.mesa.StatusMesa;
import com.actuation_system.mesa.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByStatus (StatusMesa statusMesa);
    Optional<Mesa> findByIdAndComandasStatus(Long mesaId, StatusComanda statusComanda);
    Optional<Mesa> findByQrCodeToken(String qrCodeToken);
}
