package com.actuation_system.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioRequestDTO {

    @NotBlank(message = "An email adress is required")
    private String nome;

    @NotBlank(message = "The name field cannot by empty")
    private String email;

    @NotBlank(message = "The password field cannot by empty")
    private String senha;

}
