package com.odontotrack.api.dto.OdontogramaDTO;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoOdontogramaDTO(
    @NotNull Long id,
    @NotNull @NotNull @Valid List<ItemOdontogramaDTO> itens
) {}
