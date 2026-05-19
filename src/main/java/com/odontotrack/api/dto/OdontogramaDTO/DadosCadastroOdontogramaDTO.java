package com.odontotrack.api.dto.OdontogramaDTO;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroOdontogramaDTO(
    @NotNull Long pacienteId,
    @NotNull Long agendamentoId,
    @NotNull @NotEmpty @Valid List<ItemOdontogramaDTO> itens
){}