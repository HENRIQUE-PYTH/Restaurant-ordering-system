package com.actuation_system.atendimento.entity;

import com.actuation_system.atendimento.StatusAtendimento;
import com.actuation_system.atendimento.TipoAtendimento;
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
@Table(name = "atendimentos")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column
    private LocalDateTime horario;

    @Enumerated(EnumType.STRING)
    private StatusAtendimento status;

    @Enumerated(EnumType.STRING)
    private TipoAtendimento tipoAtendimento;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;



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
