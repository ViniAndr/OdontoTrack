package com.odontotrack.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.odontotrack.api.dto.OdontogramaDTO.DadosAtualizacaoOdontogramaDTO;
import com.odontotrack.api.dto.OdontogramaDTO.DadosCadastroOdontogramaDTO;
import com.odontotrack.api.model.Odontograma;
import com.odontotrack.api.model.OdontogramaItem;
import com.odontotrack.api.repository.AgendamentoRepository;
import com.odontotrack.api.repository.OdontogramaRepository;
import com.odontotrack.api.repository.PacienteRepository;

@Service
public class OdontogramaService {

    @Autowired
    private OdontogramaRepository repository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<Odontograma> listarTodos() {
        return repository.findAll();
    }

    public Odontograma buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Odontograma não encontrado."));
    }

    public List<Odontograma> listarPorPaciente(Long pacienteId) {
        return repository.findAllByPacienteId(pacienteId);
    }

    public Odontograma buscarPorAgendamento(Long agendamentoId) {
        return repository.findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Odontograma não encontrado para este agendamento."));
    }

    @Transactional
    public Odontograma cadastrar(DadosCadastroOdontogramaDTO dados) {
        if (repository.existsByAgendamentoId(dados.agendamentoId())) {
            throw new RuntimeException("Já existe um odontograma cadastrado para este agendamento.");
        }

        var paciente = pacienteRepository.findById(dados.pacienteId())
                .filter(p -> p.getAtivo())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado ou inativo."));

        var agendamento = agendamentoRepository.findById(dados.agendamentoId())
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado."));

        var odontograma = new Odontograma();
        odontograma.setPaciente(paciente);
        odontograma.setAgendamento(agendamento);

        // Monta cada item e associa ao odontograma
        var itens = dados.itens().stream().map(dto -> {
            var item = new OdontogramaItem();
            item.setDente(dto.dente());
            item.setStatusDente(dto.statusDente());
            item.setOdontograma(odontograma);
            return item;
        }).toList();

        odontograma.getItens().addAll(itens);

        return repository.save(odontograma);
    }

    @Transactional
    public Odontograma atualizar(DadosAtualizacaoOdontogramaDTO dados) {
        var odontograma = repository.getReferenceById(dados.id());

        // Limpa os itens antigos e substitui pelos novos (orphanRemoval cuida da deleção)
        odontograma.getItens().clear();

        var novosItens = dados.itens().stream().map(dto -> {
            var item = new OdontogramaItem();
            item.setDente(dto.dente());
            item.setStatusDente(dto.statusDente());
            item.setOdontograma(odontograma);
            return item;
        }).toList();

        odontograma.getItens().addAll(novosItens);

        return odontograma; // @Transactional salva automaticamente
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Odontograma não encontrado.");
        }
        repository.deleteById(id);
    }
}
