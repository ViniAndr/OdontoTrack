package com.odontotrack.api.dto.OdontogramaDTO;

import com.odontotrack.api.Types.Dentes;
import com.odontotrack.api.Types.StatusDente;
import com.odontotrack.api.model.OdontogramaItem;

import jakarta.validation.constraints.NotNull;

public record ItemOdontogramaDTO(
    Long id,
    @NotNull Dentes dente,
    @NotNull StatusDente statusDente
) {
    public ItemOdontogramaDTO(OdontogramaItem item){
        this(
            item.getId(),
            item.getDente(),
            item.getStatusDente()
        );
    }
}
