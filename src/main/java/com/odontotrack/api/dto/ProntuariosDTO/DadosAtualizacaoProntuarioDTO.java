package com.odontotrack.api.dto.ProntuariosDTO;

import java.util.List;

import com.odontotrack.api.dto.OdontogramaDTO.ItemOdontogramaDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de atualização do prontuário clínico.
 *
 * Todos os campos clínicos são opcionais: apenas os campos não-nulos
 * são aplicados (PATCH semântico via PUT).
 * Se itensOdontograma for enviado, os itens do odontograma vinculado
 * são substituídos integralmente.
 */
public record DadosAtualizacaoProntuarioDTO(

        @NotNull Long id,

        String queixaPrincipal,

        String achadoClinico,

        String material,

        String orientacoesPaciente,

        String alergiasHistorico,

        // Lista completa de itens do odontograma para substituição.
        // Se nulo, o odontograma não é alterado.
        @Valid List<ItemOdontogramaDTO> itensOdontograma

) {}
