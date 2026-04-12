package com.odontotrack.api.dto;

import com.odontotrack.api.model.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

public record DadosCadastroProfissionalDTO(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
        @NotBlank String telefone,
        @NotBlank String registroProfissional, // CRO
        @NotNull Set<Perfil> perfis
) {}