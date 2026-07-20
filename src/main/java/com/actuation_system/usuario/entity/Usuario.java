package com.actuation_system.usuario.entity;

import com.actuation_system.atendimento.entity.Atendimento;
import com.actuation_system.comanda.entity.Comanda;
import com.actuation_system.usuario.PerfilUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique=true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(name = "ativo")
    private boolean ativo;

    @OneToMany(mappedBy = "usuario")
    private List<Comanda> comandas;

    @OneToMany(mappedBy = "usuario")
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
