package com.odontotrack.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.odontotrack.api.dto.AgendamentosDTO.DadosAtualizacaoAgendamentoDTO;
import com.odontotrack.api.dto.AgendamentosDTO.DadosCadastroAgendamentoDTO;
import com.odontotrack.api.model.AgendamentoConsulta;
import com.odontotrack.api.model.StatusConsulta;
import com.odontotrack.api.repository.AgendamentoRepository;
import com.odontotrack.api.repository.PacienteRepository;
import com.odontotrack.api.repository.ProfissionalRepository;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    public List<AgendamentoConsulta> listarTodos() {
        return repository.findAll();
    }

    public AgendamentoConsulta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));
    }

    public List<AgendamentoConsulta> listarPorPaciente(Long pacienteId) {
        return repository.findAllByPacienteId(pacienteId);
    }

    public List<AgendamentoConsulta> listarPorProfissional(Long profissionalId) {
        return repository.findAllByProfissionalId(profissionalId);
    }

    public List<AgendamentoConsulta> listarPorStatus(StatusConsulta status) {
        return repository.findAllByStatusConsulta(status);
    }

    @Transactional
    public AgendamentoConsulta cadastrar(DadosCadastroAgendamentoDTO dados) {
        if (dados.dataFim().isBefore(dados.dataInicio())) {
            throw new RuntimeException("A data de fim não pode ser anterior à data de início.");
        }

        var paciente = pacienteRepository.findById(dados.pacienteId())
                .filter(p -> p.getAtivo())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado ou inativo."));

        var profissional = profissionalRepository.findById(dados.profissionalId())
                .filter(p -> p.isEnabled())
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado ou inativo."));

        var agendamento = new AgendamentoConsulta();
        agendamento.setPaciente(paciente);
        agendamento.setProfissional(profissional);
        agendamento.setDataInicio(dados.dataInicio());
        agendamento.setDataFim(dados.dataFim());
        agendamento.setStatusConsulta(dados.statusConsulta() != null ? dados.statusConsulta() : StatusConsulta.AGENDADO);

        return repository.save(agendamento);
    }

    @Transactional
    public AgendamentoConsulta atualizar(DadosAtualizacaoAgendamentoDTO dados) {
        var agendamento = repository.getReferenceById(dados.id());

        if (dados.dataInicio() != null) agendamento.setDataInicio(dados.dataInicio());
        if (dados.dataFim() != null) agendamento.setDataFim(dados.dataFim());
        if (dados.statusConsulta() != null) agendamento.setStatusConsulta(dados.statusConsulta());

        return agendamento;
    }

    @Transactional
    public void cancelar(Long id) {
        var agendamento = repository.getReferenceById(id);
        agendamento.setStatusConsulta(StatusConsulta.CANCELADO);
    }
}
