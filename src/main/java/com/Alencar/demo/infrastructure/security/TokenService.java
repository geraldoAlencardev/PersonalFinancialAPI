package com.Alencar.demo.infrastructure.security;

import com.Alencar.demo.model.User;
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

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            // Define o algoritmo e a "senha mestra" da sua API
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("PersonalFinancerControlAPI") // Quem está emitindo o token
                    .withSubject(user.getEmail()) // O assunto (quem é o dono do token)
                    .withClaim("id", user.getId()) // Guardamos o ID do usuário dentro do token!
                    .withExpiresAt(genExpirationDate()) // Tempo de validade
                    .sign(algorithm); // Assina e gera a String final

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("PersonalFinancerControlAPI") // Valida se fomos nós que emitimos
                    .build()
                    .verify(token) // Descriptografa e verifica a validade
                    .getSubject(); // Se der tudo certo, devolve o e-mail do usuário

        } catch (JWTVerificationException exception) {
            // Se o token for inválido, expirado ou forjado, cai aqui e retorna vazio
            return "";
        }
    }

    private Instant genExpirationDate() {
        // Define que o token expira em 2 horas a partir do momento em que foi gerado
        // O ZoneOffset.of("-03:00") garante que o horário considere o fuso do Brasil
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
