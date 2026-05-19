package com.odontotrack.api.dto.ProntuariosDTO;

import java.time.LocalDateTime;

import com.odontotrack.api.dto.OdontogramaDTO.DadosDetalhamentoOdontogramaDTO;
import com.odontotrack.api.model.ProntuarioClinicos;

/**
 * DTO de resposta do prontuário clínico.
 * Inclui todos os campos clínicos e o odontograma vinculado (se existir).
 */
public record DadosDetalhamentoProntuarioDTO(

        Long id,
        Long pacienteId,
        String nomePaciente,
        Long agendamentoId,
        String alergiasHistorico,
        LocalDateTime dataUltimaAtualizacao,

        // Campos clínicos preenchidos pelo dentista
        String queixaPrincipal,
        String achadoClinico,
        String materialUsado,
        String orientacoesPaciente,

        // Odontograma vinculado — nulo se ainda não preenchido
        DadosDetalhamentoOdontogramaDTO odontograma

) {
    public DadosDetalhamentoProntuarioDTO(ProntuarioClinicos prontuario) {
        this(
                prontuario.getId(),
                prontuario.getPaciente().getId(),
                prontuario.getPaciente().getNome(),
                prontuario.getAgendamento().getId(),
                prontuario.getAlergiasHistorico(),
                prontuario.getDataUltimaAtualizacao(),
                prontuario.getQueixaPrincipal(),
                prontuario.getAchadoClinico(),
                prontuario.getMaterialUsado(),
                prontuario.getOrientacoesPaciente(),
                prontuario.getOdontograma() != null
                        ? new DadosDetalhamentoOdontogramaDTO(prontuario.getOdontograma())
                        : null
        );
    }
}
