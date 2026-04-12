package com.odontotrack.api.dto;

import com.odontotrack.api.model.Profissional;

public record DadosDetalhamentoProfissionalDTO(
        Long id,
        String nome,
        String email,
        String cpf,
        String registroProfissional,
        boolean ativo
) {
    // constructor
    public DadosDetalhamentoProfissionalDTO(Profissional profissional) {
        this(
                profissional.getId(),
                profissional.getNome(),
                profissional.getEmail(),
                profissional.getCpf(),
                profissional.getRegistroProfissional(),
                profissional.getAtivo()
        );
    }
}