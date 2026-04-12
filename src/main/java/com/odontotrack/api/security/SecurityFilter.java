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
        // 1. Pega o token do cabeçalho da requisição (se existir)
        var token = this.recuperarToken(request);

        // 2. Tenta validar o token e extrair o e-mail (subject)
        var email = tokenService.validarToken(token);

        if (email != null && !email.isEmpty()) {
            // 3. Se o token for válido, vai no banco buscar o Profissional
            Profissional profissional = repository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Profissional não encontrado no banco"));

            // 4. Cria o passe VIP. Olha como ficou mais simples!
            // O próprio profissional já entrega as "authorities" (ROLE_ADMIN, etc) dele.
            var authentication = new UsernamePasswordAuthenticationToken(profissional, null, profissional.getAuthorities());

            // 5. Avisa ao Spring: "Esse cara está logado nesta requisição"
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 6. Manda a requisição seguir o fluxo (ir para o Controller ou ser bloqueada)
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}