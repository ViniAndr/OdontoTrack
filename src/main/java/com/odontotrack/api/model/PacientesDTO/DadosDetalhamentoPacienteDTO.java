package com.odontotrack.api.model.PacientesDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.odontotrack.api.model.Paciente;

public record DadosDetalhamentoPacienteDTO(
        Long id,
        String nome,
        String cpf,
        String telefone,
        String email,
        LocalDate dataNascimento,
        LocalDateTime dataCadastro
) {
    public DadosDetalhamentoPacienteDTO(Paciente paciente) {
        this(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getTelefone(),
                paciente.getEmail(),
                paciente.getDataNascimento(),
                paciente.getDataCadastro()
        );
    }
}
