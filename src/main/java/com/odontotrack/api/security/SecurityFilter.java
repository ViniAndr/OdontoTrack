package com.odontotrack.api.security;

import com.odontotrack.api.model.Profissional;
import com.odontotrack.api.repository.ProfissionalRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProfissionalRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Pega o token do cabeçalho da requisição (se existir)
        var token = this.recuperarToken(request);

        if (token == null && !request.getRequestURI().contains("/login")) {
            // Escrevemos a mensagem de erro diretamente na resposta do servidor!
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Status 403
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Acesso bloqueado: Nenhum Token JWT foi enviado no cabeçalho.");
            return; // Corta a requisição aqui, nem tenta continuar!
        }

        // Tenta validar o token e extrair o e-mail
        var email = tokenService.validarToken(token);

        if (email != null && !email.isEmpty()) {
            // Se o token for válido, vai no banco buscar o Profissional
            Profissional profissional = repository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Profissional não encontrado no banco"));

            // O próprio profissional já entrega as "authorities" (ROLE_ADMIN, etc) dele.
            var authentication = new UsernamePasswordAuthenticationToken(profissional, null, profissional.getAuthorities());

            // Avisa ao Spring: "Esse cara está logado nesta requisição"
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Manda a requisição seguir o fluxo (ir para o Controller ou ser bloqueada)
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}