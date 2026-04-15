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

import com.odontotrack.api.dto.ProntuariosDTO.DadosAtualizacaoProntuarioDTO;
import com.odontotrack.api.dto.ProntuariosDTO.DadosCadastroProntuarioDTO;
import com.odontotrack.api.dto.ProntuariosDTO.DadosDetalhamentoProntuarioDTO;
import com.odontotrack.api.service.ProntuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    @Autowired
    private ProntuarioService service;

    // GET /prontuarios — listar todos os prontuários
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<List<DadosDetalhamentoProntuarioDTO>> listarTodos() {
        var lista = service.listarTodos().stream()
                .map(DadosDetalhamentoProntuarioDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // GET /prontuarios/{id} — buscar prontuário por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoProntuarioDTO> buscarPorId(@PathVariable Long id) {
        var prontuario = service.buscarPorId(id);
        return ResponseEntity.ok(new DadosDetalhamentoProntuarioDTO(prontuario));
    }

    // GET /prontuarios/paciente/{pacienteId} — listar prontuários de um paciente
    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<List<DadosDetalhamentoProntuarioDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        var lista = service.listarPorPaciente(pacienteId).stream()
                .map(DadosDetalhamentoProntuarioDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // POST /prontuarios — cadastrar novo prontuário (201 Created)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoProntuarioDTO> cadastrar(
            @RequestBody @Valid DadosCadastroProntuarioDTO dados,
            UriComponentsBuilder uriBuilder) {
        var prontuario = service.cadastrar(dados);
        URI uri = uriBuilder.path("/prontuarios/{id}").buildAndExpand(prontuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoProntuarioDTO(prontuario));
    }

    // PUT /prontuarios — atualizar prontuário
    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoProntuarioDTO> atualizar(
            @RequestBody @Valid DadosAtualizacaoProntuarioDTO dados) {
        var prontuario = service.atualizar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoProntuarioDTO(prontuario));
    }

    // DELETE /prontuarios/{id} — excluir prontuário (hard delete, 204 No Content)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
