package com.actuation_system.atendimento.entity;

import com.actuation_system.atendimento.StatusAtendimento;
import com.actuation_system.usuario.entity.Usuario;
import com.actuation_system.mesa.entity.Mesa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Atendimentos")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column
    private LocalDateTime horario;

    @Enumerated(EnumType.STRING)
    private StatusAtendimento status;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Atendimento that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
