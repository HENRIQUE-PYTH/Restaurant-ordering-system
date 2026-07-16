package com.actuation_system.mesa.service;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.exceptions.NotFoundException;
import com.actuation_system.mesa.StatusMesa;
import com.actuation_system.mesa.dto.MesaRequestDTO;
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

    public List<Mesa> getAllTable(){
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


    public Mesa findByTableId (Long id){
        return mesaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Table not found"));
    }

    Optional<Mesa> hasActiveOrder (Long id){
        return mesaRepository.findByIdAndComandasStatus(
                id, StatusComanda.ABERTA
        );
    }

    @Transactional
    public Mesa criarMesa(Mesa mesa) {

        // Cria a mesa
        Mesa table = new Mesa();
        mesa.setNumeroMesa(mesa.getNumeroMesa());

        // Salva para obter o ID
        mesa = mesaRepository.save(mesa);

        // Gera um token único para o QR Code
        String token = UUID.randomUUID().toString();

        // Salva o token na mesa
        mesa.setQrCode(token);

        mesa.setStatus(StatusMesa.DISPONIVEL);

        mesa = mesaRepository.save(mesa);

        // Gera a imagem do QR Code
        qrCodeService.gerarQrCode(token);

        return mesa;
    }




}
