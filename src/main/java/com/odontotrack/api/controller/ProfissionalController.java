package com.odontotrack.api.controller;

import com.odontotrack.api.dto.DadosDetalhamentoProfissionalDTO;
import com.odontotrack.api.dto.LoginDTO;
import com.odontotrack.api.dto.TokenJWTDTO;
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
    public ResponseEntity<List<DadosDetalhamentoProfissionalDTO>> listarTodos() {
        // Pega a lista do banco, transforma cada Profissional no nosso DTO limpo, e devolve a lista final
        var lista = service.listarTodosAtivos().stream()
                .map(DadosDetalhamentoProfissionalDTO::new)
                .toList();

        return ResponseEntity.ok(lista); // Retorna Status 200 OK com a lista no corpo
    }

    // ROTA: Fazer o Login (POST)
    @PostMapping("/login")
    public ResponseEntity<TokenJWTDTO> efetuarLogin(@RequestBody @Valid LoginDTO dadosLogin) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dadosLogin.email(), dadosLogin.senha());
        var authentication = manager.authenticate(authenticationToken);

        Profissional usuarioLogado = (Profissional) authentication.getPrincipal();
        String tokenJWT = tokenService.gerarToken(usuarioLogado);

        // Devolve o token empacotado no nosso DTO!
        return ResponseEntity.ok(new TokenJWTDTO(tokenJWT));
    }
}