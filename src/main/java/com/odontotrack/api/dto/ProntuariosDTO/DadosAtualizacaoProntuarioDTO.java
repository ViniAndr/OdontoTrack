package com.odontotrack.api.dto.ProntuariosDTO;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoProntuarioDTO(
        @NotNull Long id,
        String alergiasHistorico,
        String estadoOdontograma
) {}
