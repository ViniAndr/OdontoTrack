package com.odontotrack.api.service;

import com.odontotrack.api.dto.DadosAtualizacaoProfissionalDTO;
import com.odontotrack.api.dto.DadosCadastroProfissionalDTO;
import com.odontotrack.api.model.Profissional;
import com.odontotrack.api.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Profissional> listarTodosAtivos() {
        return repository.findAllByAtivoTrue();
    }

    @Transactional
    public Profissional cadastrar(DadosCadastroProfissionalDTO dados) {
        if (repository.existsByCpf(dados.cpf())) {
            throw new RuntimeException("Já existe um profissional cadastrado com este CPF.");
        }

        var profissional = new Profissional();
        profissional.setNome(dados.nome());
        profissional.setEmail(dados.email());
        profissional.setCpf(dados.cpf());
        profissional.setTelefone(dados.telefone());
        profissional.setRegistroProfissional(dados.registroProfissional());
        profissional.setPerfis(dados.perfis());
        profissional.setAtivo(true);

        // CRIPTOGRAFIA: Nunca esqueça de codificar a senha!
        profissional.setSenha(passwordEncoder.encode(dados.senha()));

        return repository.save(profissional);
    }

    public Profissional buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado."));
    }

    @Transactional
    public Profissional atualizar(DadosAtualizacaoProfissionalDTO dados) {
        var profissional = repository.getReferenceById(dados.id());

        if (dados.nome() != null) profissional.setNome(dados.nome());
        if (dados.telefone() != null) profissional.setTelefone(dados.telefone());

        return profissional; // O Spring salva automaticamente ao fim do método @Transactional
    }

    @Transactional
    public void desativar(Long id) {
        var profissional = repository.getReferenceById(id);
        profissional.setAtivo(false);
    }
}