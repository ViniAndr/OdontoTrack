package com.odontotrack.api.dto.AgendamentosDTO;

public record ResumoAgendamentosDTO(
        long agendados,
        long pendentes,
        long concluidos,
        long cancelados
) {}