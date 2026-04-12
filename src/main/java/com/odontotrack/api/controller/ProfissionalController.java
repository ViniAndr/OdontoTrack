package com.odontotrack.api.controller;

import com.odontotrack.api.dto.LoginDTO;
import com.odontotrack.api.model.Profissional;
import com.odontotrack.api.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // essa classe responde requisições da web com JSON
@RequestMapping("/profissionais") // rota base: localhost:8080/profissionais
public class ProfissionalController {

    @Autowired
    private ProfissionalService service;

    // Injetar o Gerente de Segurança do Spring
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private com.odontotrack.api.security.TokenService tokenService;

    // ROTA: Listar todos os profissionais (GET)
    @GetMapping
    public ResponseEntity<List<Profissional>> listarTodos() {
        List<Profissional> lista = service.listarTodosAtivos();
        return ResponseEntity.ok(lista); // Retorna Status 200 OK com a lista no corpo
    }

    // ROTA: Fazer o Login (POST)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO dadosLogin) {
        // um "token" temporário só com email e senha para o Spring Security analisar
        var authenticationToken = new UsernamePasswordAuthenticationToken(dadosLogin.email(), dadosLogin.senha());

        // O manager vai automaticamente usar o AutenticacaoService para buscar no banco
        // e usar o BCrypt para comparar a senha. Se errar a senha, ele já barra aqui!
        var authentication = manager.authenticate(authenticationToken);

        // pegamos o usuário que o Manager logou na memória
        Profissional usuarioLogado = (Profissional) authentication.getPrincipal();

        // E mandamos fabricar o crachá!
        String tokenJWT = tokenService.gerarToken(usuarioLogado);

        // Devolve o crachá (Token) pro Front-end!
        return ResponseEntity.ok(tokenJWT);
    }
}