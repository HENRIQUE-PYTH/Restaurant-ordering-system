package com.actuation_system.mesa.entity;

import com.actuation_system.atendimento.entity.Atendimento;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.mesa.StatusMesa;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "mesas")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private Integer numeroMesa;

    @Enumerated(EnumType.STRING)
    private StatusMesa status;

    @Column(unique=true)
    private String qrCodeToken;

    @OneToMany(mappedBy="mesa")
    private List<Comanda> comandas;

    @OneToMany(mappedBy="mesa")
    private List<Atendimento> atendimentos;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Mesa mesa)) return false;
        return Objects.equals(getId(), mesa.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
