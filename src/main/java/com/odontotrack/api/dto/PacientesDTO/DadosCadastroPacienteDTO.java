package com.odontotrack.api.dto.PacientesDTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosCadastroPacienteDTO(
        @NotBlank String nome,
        @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
        @NotBlank @Size(max = 14) String telefone,
        @NotBlank @Email String email,
        @NotBlank String endereco,
        @NotNull LocalDate dataNascimento
) {}
