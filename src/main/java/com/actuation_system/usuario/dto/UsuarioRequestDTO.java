package com.actuation_system.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @NotBlank(message = "The name field cannot be empty.")
        String nome,

        @NotBlank(message = "The email address is required.")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "The password field cannot be empty")
        @Size(min = 8, message = "The password must contain at least 8 characters.")
        String senha
) {}
