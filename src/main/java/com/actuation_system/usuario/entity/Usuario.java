package com.actuation_system.usuario.entity;

import com.actuation_system.atendimento.entity.Atendimento;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.usuario.PerfilUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "garcons")
public class Usuario {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String nome;

    @Column(unique=true)
    private String email;

    @Column
    private String senha;

    @OneToMany(mappedBy = "garcom")
    private List<Comanda> comandas;

    @OneToMany(mappedBy = "garcom")
    private List<Atendimento> atendimentos;

    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfil;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(getId(), usuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
