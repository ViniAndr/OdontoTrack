package com.odontotrack.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.odontotrack.api.dto.PacientesDTO.DadosAtualizacaoPacienteDTO;
import com.odontotrack.api.dto.PacientesDTO.DadosCadastroPacienteDTO;
import com.odontotrack.api.model.Paciente;
import com.odontotrack.api.repository.PacienteRepository;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    public List<Paciente> listarTodosAtivos() {
        return repository.findAllByAtivoTrue();
    }

    public Paciente buscarPorId(Long id) {
        return repository.findById(id)
                .filter(Paciente::getAtivo)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado ou inativo."));
    }

    @Transactional
    public Paciente cadastrar(DadosCadastroPacienteDTO dados) {
        if (repository.existsByCpf(dados.cpf())) {
            throw new RuntimeException("Já existe um paciente cadastrado com este CPF.");
        }
        if (repository.existsByEmail(dados.email())) {
            throw new RuntimeException("Já existe um paciente cadastrado com este e-mail.");
        }

        var paciente = new Paciente();
        paciente.setNome(dados.nome());
        paciente.setCpf(dados.cpf());
        paciente.setTelefone(dados.telefone());
        paciente.setEmail(dados.email());
        paciente.setEndereco(dados.endereco());
        paciente.setDataNascimento(dados.dataNascimento());

        return repository.save(paciente);
    }

    @Transactional
    public Paciente atualizar(DadosAtualizacaoPacienteDTO dados) {
        var paciente = repository.getReferenceById(dados.id());

        if (dados.nome() != null) paciente.setNome(dados.nome());
        if (dados.telefone() != null) paciente.setTelefone(dados.telefone());
        if (dados.email() != null) paciente.setEmail(dados.email());
        if (dados.endereco() != null) paciente.setEndereco(dados.endereco());
        if (dados.dataNascimento() != null) paciente.setDataNascimento(dados.dataNascimento());

        return paciente;
    }

    @Transactional
    public void desativar(Long id) {
        var paciente = repository.getReferenceById(id);
        paciente.setAtivo(false);
    }
}
