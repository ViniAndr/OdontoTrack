package com.odontotrack.api.dto.ProntuariosDTO;
import java.time.LocalDateTime;
import java.util.List;

import com.odontotrack.api.dto.OdontogramaDTO.ItemOdontogramaDTO;
import com.odontotrack.api.model.StatusConsulta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;


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
        @Null String alergiasHistorico,
        String orientacoesPaciente,
        String material,
        @NotNull LocalDateTime dataInicio,
        LocalDateTime dataFim,
        @NotNull StatusConsulta statusConsulta,
        @Valid List<ItemOdontogramaDTO> itensOdontograma
) {}
