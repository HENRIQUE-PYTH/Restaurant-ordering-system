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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comandas")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private StatusComanda status;

    @Column
    private LocalDateTime abertura;

    @Column
    private LocalDateTime fechamento;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "comanda", fetch = FetchType.EAGER)
    private List<Pedido> pedidos = new ArrayList<>();


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


