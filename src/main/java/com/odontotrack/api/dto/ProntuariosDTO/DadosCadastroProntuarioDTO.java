package com.odontotrack.api.dto.ProntuariosDTO;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroProntuarioDTO(
        @NotNull Long pacienteId,
        String alergiasHistorico,
        String estadoOdontograma
) {}
