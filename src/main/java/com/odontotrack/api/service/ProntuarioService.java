package com.odontotrack.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.odontotrack.api.dto.ProntuariosDTO.DadosAtualizacaoProntuarioDTO;
import com.odontotrack.api.dto.ProntuariosDTO.DadosCadastroProntuarioDTO;
import com.odontotrack.api.model.Odontograma;
import com.odontotrack.api.model.OdontogramaItem;
import com.odontotrack.api.model.ProntuarioClinicos;
import com.odontotrack.api.repository.AgendamentoRepository;
import com.odontotrack.api.repository.OdontogramaRepository;
import com.odontotrack.api.repository.ProntuarioRepository;

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

    /**
     * Cria um prontuário clínico a partir de um agendamento.
     *
     * Regras:
     * - Um agendamento só pode ter um prontuário (1:1).
     * - Paciente e profissional são derivados do agendamento — não precisam ser
     *   enviados pelo frontend.
     * - Se itensOdontograma for enviado, o odontograma é criado e vinculado
     *   automaticamente na mesma transação.
     */
    @Transactional
    public ProntuarioClinicos cadastrar(DadosCadastroProntuarioDTO dados) {

        // Garante unicidade: um agendamento → um prontuário
        if (repository.existsByAgendamentoId(dados.agendamentoId())) {
            throw new RuntimeException("Já existe um prontuário cadastrado para este agendamento.");
        }

        var agendamento = agendamentoRepository.findById(dados.agendamentoId())
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        // Paciente derivado do agendamento
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

        // Cria e vincula odontograma se itens forem enviados
        if (dados.itensOdontograma() != null && !dados.itensOdontograma().isEmpty()) {

            // Valida que já não existe odontograma para este agendamento
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

            var odontogramaSalvo = odontogramaRepository.save(odontograma);
            prontuario.setOdontograma(odontogramaSalvo);
        }

        return repository.save(prontuario);
    }

    // ─────────────────────────────────────────────
    // Atualização
    // ─────────────────────────────────────────────

    /**
     * Atualiza os campos clínicos do prontuário.
     *
     * Aplica apenas os campos não-nulos (semântica de PATCH via PUT).
     * Se itensOdontograma for enviado, os itens do odontograma vinculado
     * são substituídos integralmente; se ainda não existir odontograma,
     * um novo é criado.
     */
    @Transactional
    public ProntuarioClinicos atualizar(DadosAtualizacaoProntuarioDTO dados) {
        var prontuario = repository.getReferenceById(dados.id());

        if (dados.queixaPrincipal() != null)    prontuario.setQueixaPrincipal(dados.queixaPrincipal());
        if (dados.achadoClinico() != null)       prontuario.setAchadoClinico(dados.achadoClinico());
        if (dados.material() != null)            prontuario.setMaterialUsado(dados.material());
        if (dados.orientacoesPaciente() != null) prontuario.setOrientacoesPaciente(dados.orientacoesPaciente());
        if (dados.alergiasHistorico() != null)   prontuario.setAlergiasHistorico(dados.alergiasHistorico());

        // Atualiza odontograma se itens forem enviados
        if (dados.itensOdontograma() != null && !dados.itensOdontograma().isEmpty()) {

            Odontograma odontograma;

            if (prontuario.getOdontograma() != null) {
                // Substitui os itens do odontograma existente
                odontograma = prontuario.getOdontograma();
                odontograma.getItens().clear();
            } else {
                // Cria novo odontograma e vincula ao prontuário
                odontograma = new Odontograma();
                odontograma.setPaciente(prontuario.getPaciente());
                odontograma.setAgendamento(prontuario.getAgendamento());
                prontuario.setOdontograma(odontograma);
            }

            var novosItens = dados.itensOdontograma().stream().map(dto -> {
                var item = new OdontogramaItem();
                item.setDente(dto.dente());
                item.setStatusDente(dto.statusDente());
                item.setOdontograma(odontograma);
                return item;
            }).toList();

            odontograma.getItens().addAll(novosItens);
        }

        // @Transactional persiste automaticamente via dirty-checking
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
