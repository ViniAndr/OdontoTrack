package com.odontotrack.api.dto.AgendamentosDTO;

import java.time.LocalDateTime;

import com.odontotrack.api.model.StatusConsulta;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroAgendamentoDTO(
        @NotNull Long pacienteId,
        @NotNull Long profissionalId,
        @NotNull LocalDateTime dataInicio,
        @NotNull LocalDateTime dataFim,
        StatusConsulta statusConsulta
) {}
