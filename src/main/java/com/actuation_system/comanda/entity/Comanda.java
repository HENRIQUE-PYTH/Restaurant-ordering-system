package com.actuation_system.comanda.entity;

import com.actuation_system.comanda.StatusComanda;
import com.actuation_system.usuario.entity.Usuario;
import com.actuation_system.mesa.entity.Mesa;
import com.actuation_system.pedido.entity.Pedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tabelas")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private StatusComanda status;

    @Column
    private LocalDateTime abertura;

    @Column
    private LocalDateTime fechamento;

    @OneToMany(mappedBy = "comanda")
    private List<Pedido> pedidos;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Comanda comanda)) return false;
        return Objects.equals(getId(), comanda.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}


