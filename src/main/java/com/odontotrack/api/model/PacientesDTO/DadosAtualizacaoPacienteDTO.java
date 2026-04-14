package com.odontotrack.api.model.PacientesDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPacienteDTO(
        @NotNull Long id,
        String nome,
        String telefone,
        @Email String email
) {}
