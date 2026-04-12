package com.odontotrack.api.controller;

import com.odontotrack.api.dto.*;
import com.odontotrack.api.model.Profissional;
import com.odontotrack.api.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.AccessDeniedException;
import com.odontotrack.api.model.Perfil;

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
    @PreAuthorize("hasRole('ADMIN')") // SÓ ENTRA SE TIVER O PERFIL 'ROLE_ADMIN'
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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // SÓ ENTRA SE TIVER O PERFIL 'ROLE_ADMIN'
    public ResponseEntity<DadosDetalhamentoProfissionalDTO> cadastrar(@RequestBody @Valid DadosCadastroProfissionalDTO dados) {
        var profissional = service.cadastrar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoProfissionalDTO(profissional));
    }

    @PutMapping
    public ResponseEntity<DadosDetalhamentoProfissionalDTO> atualizar(
            @RequestBody @Valid DadosAtualizacaoProfissionalDTO dados,
            @AuthenticationPrincipal Profissional usuarioLogado // O Spring pega o Token e injeta o dono dele aqui!
            ) {
        // Descobrimos se o cara logado é um Admin
        boolean isAdmin = usuarioLogado.getPerfis().contains(Perfil.ROLE_ADMIN);

        // A Regra de Ouro: Se NÃO for Admin E estiver tentando alterar o ID de outro usuário...
        if (!isAdmin && !usuarioLogado.getId().equals(dados.id())) {
            throw new AccessDeniedException("Você só tem permissão para alterar seus próprios dados.");
        }

        // Se passou do if acima, ou é Admin, ou é o próprio dono dos dados. Pode salvar!
        var profissional = service.atualizar(dados);
        return ResponseEntity.ok(new DadosDetalhamentoProfissionalDTO(profissional));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // SÓ ENTRA SE TIVER O PERFIL 'ROLE_ADMIN'
    public ResponseEntity desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}