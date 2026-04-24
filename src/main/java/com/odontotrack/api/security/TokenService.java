package com.odontotrack.api.security;

import com.odontotrack.api.model.Profissional;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Puxa a senha secreta que coloquei no application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Profissional profissional){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("api-odontotrack") // Mudei o nome do emissor
                    .withSubject(profissional.getEmail()) // Aqui usamos o e-mail
                    .withClaim("id", profissional.getId())
                    .withClaim("nome", profissional.getNome())
                    .withClaim(
                            "perfis",
                            profissional.getPerfis()
                                    .stream()
                                    .map(Enum::name)
                                    .toList()
                    )
                    .withExpiresAt(this.gerarExpiracaoJWT())
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token jwt", exception);
        }
    }

    public String validarToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("api-odontotrack")
                    .build()
                    .verify(token)
                    .getSubject(); // Se for válido, devolve o e-mail do cara!
        }catch (JWTVerificationException exception){
            return ""; // Retorna vazio se o token for falso ou expirado
        }
    }

    private Instant gerarExpiracaoJWT(){
        // O Token vai durar 2 horas
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}