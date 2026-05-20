package com.odontotrack.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.odontotrack.api.dto.ProntuariosDTO.DadosAtualizacaoProntuarioDTO;
import com.odontotrack.api.dto.ProntuariosDTO.DadosCadastroProntuarioDTO;
import com.odontotrack.api.model.ProntuarioClinicos;
import com.odontotrack.api.model.Odontograma; // Import necessário
import com.odontotrack.api.model.OdontogramaItem; // Import necessário
import com.odontotrack.api.repository.AgendamentoRepository;
import com.odontotrack.api.repository.ProntuarioRepository;
import com.odontotrack.api.repository.OdontogramaRepository; // Import necessário

@Service
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository repository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private OdontogramaRepository odontogramaRepository;

    // ─────────────────────────────────────────────
    // Consultas
    // ─────────────────────────────────────────────

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

    public ProntuarioClinicos buscarPorAgendamento(Long agendamentoId) {
        return repository.findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Prontuário não encontrado para este agendamento."));
    }

    // ─────────────────────────────────────────────
    // Criação
    // ─────────────────────────────────────────────

    @Transactional
    public ProntuarioClinicos cadastrar(DadosCadastroProntuarioDTO dados) {
        if (repository.existsByAgendamentoId(dados.agendamentoId())) {
            throw new RuntimeException("Já existe um prontuário cadastrado para este agendamento.");
        }

        var agendamento = agendamentoRepository.findById(dados.agendamentoId())
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        var paciente = agendamento.getPaciente();
        if (!paciente.getAtivo()) {
            throw new RuntimeException("Paciente inativo.");
        }

        var prontuario = new ProntuarioClinicos();
        prontuario.setPaciente(paciente);
        prontuario.setAgendamento(agendamento);
        prontuario.setQueixaPrincipal(dados.queixaPrincipal());
        prontuario.setAchadoClinico(dados.achadoClinico());
        prontuario.setMaterialUsado(dados.material());
        prontuario.setOrientacoesPaciente(dados.orientacoesPaciente());
        prontuario.setAlergiasHistorico(dados.alergiasHistorico());

        if (dados.itensOdontograma() != null && !dados.itensOdontograma().isEmpty()) {
            if (odontogramaRepository.existsByAgendamentoId(dados.agendamentoId())) {
                throw new RuntimeException("Já existe um odontograma para este agendamento.");
            }

            var odontograma = new Odontograma();
            odontograma.setPaciente(paciente);
            odontograma.setAgendamento(agendamento);

            var itens = dados.itensOdontograma().stream().map(dto -> {
                var item = new OdontogramaItem();
                item.setDente(dto.dente());
                item.setStatusDente(dto.statusDente());
                item.setOdontograma(odontograma);
                return item;
            }).toList();

            odontograma.getItens().addAll(itens);
            
            // Força a persistência explicitamente para evitar problemas de dependência transiente
            var odontogramaSalvo = odontogramaRepository.save(odontograma);
            prontuario.setOdontograma(odontogramaSalvo);
        }

        return repository.save(prontuario);
    }

    // ─────────────────────────────────────────────
    // Atualização
    // ─────────────────────────────────────────────

    @Transactional
    public ProntuarioClinicos atualizar(DadosAtualizacaoProntuarioDTO dados) {
        var prontuario = repository.findById(dados.id())
                .orElseThrow(() -> new RuntimeException("Prontuário não encontrado."));

        if (dados.queixaPrincipal() != null)    prontuario.setQueixaPrincipal(dados.queixaPrincipal());
        if (dados.achadoClinico() != null)       prontuario.setAchadoClinico(dados.achadoClinico());
        if (dados.material() != null)            prontuario.setMaterialUsado(dados.material());
        if (dados.orientacoesPaciente() != null) prontuario.setOrientacoesPaciente(dados.orientacoesPaciente());
        if (dados.alergiasHistorico() != null)   prontuario.setAlergiasHistorico(dados.alergiasHistorico());

        if (dados.itensOdontograma() != null && !dados.itensOdontograma().isEmpty()) {
            Odontograma odontograma;

            if (prontuario.getOdontograma() != null) {
                odontograma = prontuario.getOdontograma();
                odontograma.getItens().clear();
            } else {
                odontograma = new Odontograma();
                odontograma.setPaciente(prontuario.getPaciente());
                odontograma.setAgendamento(prontuario.getAgendamento());
                // Importante: salva o novo odontograma antes de injetar na entidade pai se não houver Cascade estruturado
                odontograma = odontogramaRepository.save(odontograma);
                prontuario.setOdontograma(odontograma);
            }

            // Variável final para uso dentro do escopo do lambda lambda
            final Odontograma odontogramaRef = odontograma;
            var novosItens = dados.itensOdontograma().stream().map(dto -> {
                var item = new OdontogramaItem();
                item.setDente(dto.dente());
                item.setStatusDente(dto.statusDente());
                item.setOdontograma(odontogramaRef);
                return item;
            }).toList();

            odontograma.getItens().addAll(novosItens);
        }

        return prontuario;
    }

    // ─────────────────────────────────────────────
    // Exclusão
    // ─────────────────────────────────────────────

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Prontuário não encontrado.");
        }
        repository.deleteById(id);
    }
}