package com.odontotrack.api.dto.AgendamentosDTO;

import java.time.LocalDateTime;

import com.odontotrack.api.model.AgendamentoConsulta;
import com.odontotrack.api.model.StatusConsulta;

public record DadosDetalhamentoAgendamentoDTO(
        Long id,
        Long pacienteId,
        String nomePaciente,
        Long profissionalId,
        String nomeProfissional,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        StatusConsulta statusConsulta
) {
    public DadosDetalhamentoAgendamentoDTO(AgendamentoConsulta agendamento) {
        this(
                agendamento.getId(),
                agendamento.getPaciente().getId(),
                agendamento.getPaciente().getNome(),
                agendamento.getProfissional().getId(),
                agendamento.getProfissional().getNome(),
                agendamento.getDataInicio(),
                agendamento.getDataFim(),
                agendamento.getStatusConsulta()
        );
    }
}
