package com.Alencar.demo.infrastructure.security;

import com.Alencar.demo.model.User;
import com.Alencar.demo.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {

        // 1. Recupera o token do cabeçalho da requisição
        String token = this.recoverToken(request);

        // 2. Se houver um token, vamos validá-lo
        if (token != null) {
            // O validateToken devolve o e-mail do usuário se o token estiver correto
            String login = tokenService.validateToken(token);

            if (!login.isEmpty()) {
                // 3. Busca o usuário no banco pelo e-mail salvo no token
                Optional<User> userOptional = userRepository.findByEmail(login);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();

                    // 4. Cria o objeto de autenticação do Spring
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

                    // 5. Salva a autenticação no contexto do Spring Security
                    // É ISSO AQUI que permite a gente tirar o @RequestHeader("X-User-Id") dos controllers depois!
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // 6. Passa a requisição para o próximo filtro (ou para o Controller, se estiver tudo certo)
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair o token do cabeçalho "Authorization"
    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        // O padrão JWT exige que o token venha escrito como "Bearer seu-token-aqui"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        // Corta a palavra "Bearer " e devolve só o token
        return authHeader.replace("Bearer ", "");
    }
}
