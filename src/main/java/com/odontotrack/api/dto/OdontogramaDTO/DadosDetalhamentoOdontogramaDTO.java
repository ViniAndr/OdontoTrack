package com.odontotrack.api.dto.OdontogramaDTO;

import com.odontotrack.api.model.Odontograma;

import java.util.List;

public record DadosDetalhamentoOdontogramaDTO(
        Long id,
        Long pacienteId,
        String nomePaciente,
        Long agendamentoId,
        List<ItemOdontogramaDTO> itens
) {
    public DadosDetalhamentoOdontogramaDTO(Odontograma odontograma) {
        this(
                odontograma.getId(),
                odontograma.getPaciente().getId(),
                odontograma.getPaciente().getNome(),
                odontograma.getAgendamento().getId(),
                odontograma.getItens().stream()
                        .map(ItemOdontogramaDTO::new)
                        .toList()
        );
    }
}
