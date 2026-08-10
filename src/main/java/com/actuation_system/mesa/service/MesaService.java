package com.actuation_system.mesa.service;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.comanda.repository.ComandaRepository;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.mesa.StatusMesa;
import com.actuation_system.mesa.entity.Mesa;
import com.actuation_system.mesa.repository.MesaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ComandaRepository comandaRepository;

    public List<Mesa> getAllTables(){
        return mesaRepository.findAll();
    }

    public List<Mesa> searchAvailableTables (){
        return mesaRepository.findByStatus(StatusMesa.DISPONIVEL);
    }

    public List<Mesa> findOccupiedTables (){
        return mesaRepository.findByStatus(StatusMesa.OCUPADA);
    }

    public List<Mesa> findClosingTables (){
        return mesaRepository.findByStatus(StatusMesa.FECHANDO);
    }


    public Mesa findById (Long id){
        return mesaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Table not found"));
    }

    public boolean hasActiveOrder (Long id){
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Table not found"));
        return comandaRepository.existsByMesaAndStatus(mesa, StatusComanda.ABERTA);
    }

    public Mesa findByQrCodeToken(String token) {

        return mesaRepository.findByQrCodeToken(token)
                .orElseThrow(() -> new NotFoundException("Table not found."));
    }

    @Transactional
    public Mesa createTable(Mesa mesa) {
        mesa.setStatus(StatusMesa.DISPONIVEL);
        mesa.setQrCodeToken(UUID.randomUUID().toString());
        return mesaRepository.save(mesa);
    }


}
