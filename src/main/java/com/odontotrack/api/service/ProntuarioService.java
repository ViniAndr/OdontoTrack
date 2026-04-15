package com.odontotrack.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.odontotrack.api.dto.ProntuariosDTO.DadosAtualizacaoProntuarioDTO;
import com.odontotrack.api.dto.ProntuariosDTO.DadosCadastroProntuarioDTO;
import com.odontotrack.api.model.ProntuarioClinicos;
import com.odontotrack.api.repository.PacienteRepository;
import com.odontotrack.api.repository.ProntuarioRepository;

@Service
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository repository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<ProntuarioClinicos> listarTodos() {
        return repository.findAll();
    }

    public ProntuarioClinicos buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prontuário não encontrado."));
    }

    public List<ProntuarioClinicos> listarPorPaciente(Long pacienteId) {
        return repository.findAllByPacienteId(pacienteId);
    }

    @Transactional
    public ProntuarioClinicos cadastrar(DadosCadastroProntuarioDTO dados) {
        var paciente = pacienteRepository.findById(dados.pacienteId())
                .filter(p -> p.getAtivo())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado ou inativo."));

        var prontuario = new ProntuarioClinicos();
        prontuario.setPaciente(paciente);
        prontuario.setAlergiasHistorico(dados.alergiasHistorico());
        prontuario.setEstadoOdontograma(dados.estadoOdontograma());

        return repository.save(prontuario);
    }

    @Transactional
    public ProntuarioClinicos atualizar(DadosAtualizacaoProntuarioDTO dados) {
        var prontuario = repository.getReferenceById(dados.id());

        if (dados.alergiasHistorico() != null) prontuario.setAlergiasHistorico(dados.alergiasHistorico());
        if (dados.estadoOdontograma() != null) prontuario.setEstadoOdontograma(dados.estadoOdontograma());

        return prontuario;
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Prontuário não encontrado.");
        }
        repository.deleteById(id);
    }
}
