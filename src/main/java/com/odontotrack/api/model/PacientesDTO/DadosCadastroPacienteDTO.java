package com.odontotrack.api.model.PacientesDTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroPacienteDTO(
        @NotBlank String nome,
        @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
        @NotBlank @Pattern(regexp = "\\d{11}") String telefone,
        @NotBlank @Email String email,
        @NotNull LocalDate dataNascimento
) {}
