package com.odontotrack.api.dto.ProntuariosDTO;

import com.odontotrack.api.dto.OdontogramaDTO.ItemOdontogramaDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


/**
 * DTO de cadastro do prontuário clínico.
 *
 * O dentista preenche este formulário ao atender o paciente.
 * O paciente e o profissional são derivados do agendamento — não é necessário
 * enviá-los separadamente.
 *
 * O odontograma é opcional: se a lista de itensOdontograma for enviada,
 * o backend cria e vincula o odontograma automaticamente na mesma transação.
 */

public record DadosCadastroProntuarioDTO(
        @NotNull Long agendamentoId,
        String queixaPrincipal,
        @NotBlank String achadoClinico,
        String alergiasHistorico,
        String orientacoesPaciente,
        String material,
        List<ItemOdontogramaDTO> itensOdontograma
) {}
