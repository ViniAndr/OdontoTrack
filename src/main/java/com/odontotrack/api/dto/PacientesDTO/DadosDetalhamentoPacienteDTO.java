package com.odontotrack.api.dto.PacientesDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.odontotrack.api.model.Paciente;

public record DadosDetalhamentoPacienteDTO(
        Long id,
        String nome,
        String cpf,
        String telefone,
        String email,
        String endereco,
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
                paciente.getEndereco(),
                paciente.getDataNascimento(),
                paciente.getDataCadastro()
        );
    }
}
