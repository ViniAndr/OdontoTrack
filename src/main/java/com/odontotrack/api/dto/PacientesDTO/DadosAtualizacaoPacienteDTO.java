package com.odontotrack.api.dto.PacientesDTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPacienteDTO(
        @NotNull Long id,
        String nome,
        String telefone,
        @Email String email,
        String endereco,
        LocalDate dataNascimento
) {}
