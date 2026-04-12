package com.odontotrack.api.dto;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoProfissionalDTO(
        @NotNull Long id,
        String nome,
        String telefone
) {}