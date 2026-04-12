package com.odontotrack.api.controller;

import com.odontotrack.api.dto.LoginDTO;
import com.odontotrack.api.model.Profissional;
import com.odontotrack.api.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // essa classe responde requisições da web com JSON
@RequestMapping("/profissionais") // rota base: localhost:8080/profissionais
public class ProfissionalController {

    @Autowired
    private ProfissionalService service;

    // ROTA: Listar todos os profissionais (GET)
    @GetMapping
    public ResponseEntity<List<Profissional>> listarTodos() {
        List<Profissional> lista = service.listarTodosAtivos();
        return ResponseEntity.ok(lista); // Retorna Status 200 OK com a lista no corpo
    }

    // ROTA: Fazer o Login (POST)
    @PostMapping("/login")
    public ResponseEntity<Profissional> login(@RequestBody @Valid LoginDTO dadosLogin) {
        // Manda o Service verificar se o email e senha batem
        Profissional profissionalLogado = service.autenticar(dadosLogin.email(), dadosLogin.senha());

        return ResponseEntity.ok(profissionalLogado);
    }
}