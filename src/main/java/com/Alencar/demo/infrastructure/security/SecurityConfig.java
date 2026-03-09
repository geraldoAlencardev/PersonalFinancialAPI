package com.Alencar.demo.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita CSRF pois nossa autenticação é baseada em Token (não usamos cookies)
                .csrf(csrf -> csrf.disable())

                // Define que a API é Stateless (não guarda sessão do usuário na memória do servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura as regras de acesso (Quem pode acessar o quê)
                .authorizeHttpRequests(authorize -> authorize
                        // Libera o cadastro de novos usuários para qualquer pessoa
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        // Libera a rota de login (que vamos criar no próximo passo)
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        //Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                        // Todas as outras rotas (transações, contas, categorias) exigem usuário logado com Token
                        .anyRequest().authenticated()
                )

                // A MÁGICA: Diz para o Spring rodar o nosso filtro JWT ANTES do filtro padrão dele
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // Bean necessário para o Controller de Login conseguir chamar a autenticação do Spring
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // O Bean que você já tinha, mantido para criptografar as senhas no UserService
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}