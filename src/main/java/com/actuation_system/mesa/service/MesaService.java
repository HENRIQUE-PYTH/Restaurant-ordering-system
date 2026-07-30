package com.actuation_system.mesa.service;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.mesa.StatusMesa;
import com.actuation_system.mesa.entity.Mesa;
import com.actuation_system.mesa.repository.MesaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;
    private final QrCodeService qrCodeService;

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

    public Optional<Mesa> hasActiveOrder (Long id){
        return mesaRepository.findByIdAndComandasStatus(
                id, StatusComanda.ABERTA);
    }

    public Mesa findByQrCodeToken(String token) {

        return mesaRepository.findByQrCodeToken(token)
                .orElseThrow(() -> new RuntimeException("Table not found."));
    }

    @Transactional
    public Mesa createTable(Mesa table) {

        Mesa mesa = new Mesa();
        mesa.setNumeroMesa(table.getNumeroMesa());
        mesa.setStatus(StatusMesa.DISPONIVEL);

        // Salva para gerar o ID
        mesa = mesaRepository.save(mesa);

        // Gera um token único
        String token = UUID.randomUUID().toString();
        mesa.setQrCodeToken(token);

        // Gera a imagem do QR Code
        qrCodeService.gerarQrCode(token);

        return mesa;
    }


}
