package com.odontotrack.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.odontotrack.api.dto.OdontogramaDTO.DadosAtualizacaoOdontogramaDTO;
import com.odontotrack.api.dto.OdontogramaDTO.DadosCadastroOdontogramaDTO;
import com.odontotrack.api.dto.OdontogramaDTO.DadosDetalhamentoOdontogramaDTO;
import com.odontotrack.api.service.OdontogramaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/odontogramas")
public class OdontogramaController {

    @Autowired
    private OdontogramaService service;

    // GET /odontogramas — listar todos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<List<DadosDetalhamentoOdontogramaDTO>> listarTodos() {
        var lista = service.listarTodos().stream()
                .map(DadosDetalhamentoOdontogramaDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // GET /odontogramas/{id} — buscar por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoOdontogramaDTO> buscarPorId(@PathVariable Long id) {
        var odontograma = service.buscarPorId(id);
        return ResponseEntity.ok(new DadosDetalhamentoOdontogramaDTO(odontograma));
    }

    // GET /odontogramas/paciente/{pacienteId} — listar todos os odontogramas de um paciente
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<List<DadosDetalhamentoOdontogramaDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        var lista = service.listarPorPaciente(pacienteId).stream()
                .map(DadosDetalhamentoOdontogramaDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // GET /odontogramas/agendamento/{agendamentoId} — buscar odontograma de uma consulta específica
    @GetMapping("/agendamento/{agendamentoId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoOdontogramaDTO> buscarPorAgendamento(@PathVariable Long agendamentoId) {
        var odontograma = service.buscarPorAgendamento(agendamentoId);
        return ResponseEntity.ok(new DadosDetalhamentoOdontogramaDTO(odontograma));
    }

    // POST /odontogramas — criar odontograma com itens (201 Created)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoOdontogramaDTO> cadastrar(
            @RequestBody @Valid DadosCadastroOdontogramaDTO dados,
            UriComponentsBuilder uriBuilder) {
        var odontograma = service.cadastrar(dados);
        URI uri = uriBuilder.path("/odontogramas/{id}").buildAndExpand(odontograma.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoOdontogramaDTO(odontograma));
    }

    // PUT /odontogramas — atualizar itens do odontograma
    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoOdontogramaDTO> atualizar(
            @RequestBody @Valid DadosAtualizacaoOdontogramaDTO dados) {
        var odontograma = service.atualizar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoOdontogramaDTO(odontograma));
    }

    // DELETE /odontogramas/{id} — excluir odontograma (hard delete, 204 No Content)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
