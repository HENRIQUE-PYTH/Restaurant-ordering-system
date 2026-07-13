package com.actuation_system.mesa.entity;

import com.actuation_system.atendimento.entity.Atendimento;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.mesa.StatusMesa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mesas")
public class Mesa {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private Integer numero;

    @Enumerated
    private StatusMesa status;

    @Column(unique=true)
    private String qrCode;

    @OneToMany(mappedBy="mesa")
    private List<Comanda> comandas;

    @OneToMany(mappedBy="mesa")
    private List<Atendimento> atendimentos;
}
