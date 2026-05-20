package com.odontotrack.api.dto.ProntuariosDTO;

import com.odontotrack.api.dto.OdontogramaDTO.ItemOdontogramaDTO;

import jakarta.validation.constraints.NotNull;

import java.util.List;

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
        List<ItemOdontogramaDTO> itensOdontograma
) {}
