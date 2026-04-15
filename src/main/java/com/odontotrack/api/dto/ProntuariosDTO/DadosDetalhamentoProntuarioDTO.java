package com.odontotrack.api.dto.ProntuariosDTO;

import java.time.LocalDateTime;

import com.odontotrack.api.model.ProntuarioClinicos;

public record DadosDetalhamentoProntuarioDTO(
        Long id,
        Long pacienteId,
        String nomePaciente,
        String alergiasHistorico,
        String estadoOdontograma,
        LocalDateTime dataUltimaAtualizacao
) {
    public DadosDetalhamentoProntuarioDTO(ProntuarioClinicos prontuario) {
        this(
                prontuario.getId(),
                prontuario.getPaciente().getId(),
                prontuario.getPaciente().getNome(),
                prontuario.getAlergiasHistorico(),
                prontuario.getEstadoOdontograma(),
                prontuario.getDataUltimaAtualizacao()
        );
    }
}
