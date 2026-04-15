package com.odontotrack.api.dto.AgendamentosDTO;

import java.time.LocalDateTime;

import com.odontotrack.api.model.StatusConsulta;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoAgendamentoDTO(
        @NotNull Long id,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        StatusConsulta statusConsulta
) {}
