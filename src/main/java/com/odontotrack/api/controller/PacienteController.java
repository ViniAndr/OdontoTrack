package com.odontotrack.api.controller;


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

import com.odontotrack.api.model.PacientesDTO.DadosAtualizacaoPacienteDTO;
import com.odontotrack.api.model.PacientesDTO.DadosCadastroPacienteDTO;
import com.odontotrack.api.model.PacientesDTO.DadosDetalhamentoPacienteDTO;
import com.odontotrack.api.service.PacienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    // ROTA: Listar todos os pacientes ativos (GET)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<List<DadosDetalhamentoPacienteDTO>> listarTodos() {
        var lista = service.listarTodosAtivos().stream()
                .map(DadosDetalhamentoPacienteDTO::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    // ROTA: Cadastrar novo paciente (POST)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoPacienteDTO> cadastrar(@RequestBody @Valid DadosCadastroPacienteDTO dados) {
        var paciente = service.cadastrar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPacienteDTO(paciente));
    }

    // ROTA: Atualizar paciente (PUT)
    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTISTA')")
    public ResponseEntity<DadosDetalhamentoPacienteDTO> atualizar(@RequestBody @Valid DadosAtualizacaoPacienteDTO dados) {
        var paciente = service.atualizar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoPacienteDTO(paciente));
    }

    // ROTA: Desativar paciente (DELETE - soft delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}
