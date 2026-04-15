package com.odontotrack.api.controller;

import java.net.URI;

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

import com.odontotrack.api.dto.PacientesDTO.DadosAtualizacaoPacienteDTO;
import com.odontotrack.api.dto.PacientesDTO.DadosCadastroPacienteDTO;
import com.odontotrack.api.dto.PacientesDTO.DadosDetalhamentoPacienteDTO;
import com.odontotrack.api.service.PacienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    // GET /pacientes — listar todos os pacientes ativos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<Object> listarTodos() {
        var lista = service.listarTodosAtivos().stream()
                .map(DadosDetalhamentoPacienteDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // GET /pacientes/{id} — buscar paciente por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoPacienteDTO> buscarPorId(@PathVariable Long id) {
        var paciente = service.buscarPorId(id);
        return ResponseEntity.ok(new DadosDetalhamentoPacienteDTO(paciente));
    }

    // POST /pacientes — cadastrar novo paciente (201 Created)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoPacienteDTO> cadastrar(
            @RequestBody @Valid DadosCadastroPacienteDTO dados,
            UriComponentsBuilder uriBuilder) {
        var paciente = service.cadastrar(dados);
        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPacienteDTO(paciente));
    }

    // PUT /pacientes — atualizar dados do paciente
    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoPacienteDTO> atualizar(
            @RequestBody @Valid DadosAtualizacaoPacienteDTO dados) {
        var paciente = service.atualizar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPacienteDTO(paciente));
    }

    // DELETE /pacientes/{id} — desativar paciente (soft delete, 204 No Content)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
