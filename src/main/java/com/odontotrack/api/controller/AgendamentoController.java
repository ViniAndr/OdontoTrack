package com.odontotrack.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.odontotrack.api.dto.AgendamentosDTO.DadosAtualizacaoAgendamentoDTO;
import com.odontotrack.api.dto.AgendamentosDTO.DadosCadastroAgendamentoDTO;
import com.odontotrack.api.dto.AgendamentosDTO.DadosDetalhamentoAgendamentoDTO;
import com.odontotrack.api.model.Profissional;
import com.odontotrack.api.model.StatusConsulta;
import com.odontotrack.api.service.AgendamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    // GET /agendamentos — listar todos (filtro opcional por status: ?status=AGENDADO)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<DadosDetalhamentoAgendamentoDTO>> listarTodos(
            @RequestParam(required = false) StatusConsulta status) {
        var lista = (status != null ? service.listarPorStatus(status) : service.listarTodos())
                .stream()
                .map(DadosDetalhamentoAgendamentoDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/meus")
    @PreAuthorize("hasRole('DENTISTA')")
    public ResponseEntity<List<DadosDetalhamentoAgendamentoDTO>> listarMinhasConsultas(
            @AuthenticationPrincipal Profissional usuarioLogado) {
        var lista = service.listarPorProfissional(usuarioLogado.getId()).stream()
                .map(DadosDetalhamentoAgendamentoDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // GET /agendamentos/{id} — buscar por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<DadosDetalhamentoAgendamentoDTO> buscarPorId(@PathVariable Long id) {
        var agendamento = service.buscarPorId(id);
        return ResponseEntity.ok(new DadosDetalhamentoAgendamentoDTO(agendamento));
    }

    // GET /agendamentos/paciente/{pacienteId} — listar por paciente
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<DadosDetalhamentoAgendamentoDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        var lista = service.listarPorPaciente(pacienteId).stream()
                .map(DadosDetalhamentoAgendamentoDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // GET /agendamentos/profissional/{profissionalId} — listar por profissional
    @GetMapping("/profissional/{profissionalId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<DadosDetalhamentoAgendamentoDTO>> listarPorProfissional(@PathVariable Long profissionalId) {
        var lista = service.listarPorProfissional(profissionalId).stream()
                .map(DadosDetalhamentoAgendamentoDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // POST /agendamentos — criar agendamento (201 Created)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<DadosDetalhamentoAgendamentoDTO> cadastrar(
            @RequestBody @Valid DadosCadastroAgendamentoDTO dados,
            UriComponentsBuilder uriBuilder) {
        var agendamento = service.cadastrar(dados);
        URI uri = uriBuilder.path("/agendamentos/{id}").buildAndExpand(agendamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoAgendamentoDTO(agendamento));
    }

    // PUT /agendamentos — atualizar agendamento
    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<DadosDetalhamentoAgendamentoDTO> atualizar(
            @RequestBody @Valid DadosAtualizacaoAgendamentoDTO dados) {
        var agendamento = service.atualizar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoAgendamentoDTO(agendamento));
    }

    // DELETE /agendamentos/{id} — cancelar agendamento (204 No Content)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
