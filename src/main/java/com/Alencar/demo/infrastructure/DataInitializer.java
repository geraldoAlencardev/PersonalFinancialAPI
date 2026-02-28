package com.Alencar.demo.infrastructure;

import com.Alencar.demo.model.Account;
import com.Alencar.demo.model.Category;
import com.Alencar.demo.model.User;
import com.Alencar.demo.repository.AccountRepository;
import com.Alencar.demo.repository.CategoryRepository;
import com.Alencar.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional // Adicione Transactional para garantir a consistência
    public void run(String... args) {

        // 1. Verificação de segurança: Só cria se o e-mail não existir
        if (userRepository.findByEmail("alencar@teste.com").isEmpty()) {

            User user = User.builder()
                    .username("alencar_dev")
                    .email("alencar@teste.com")
                    .password("123456")
                    .build();
            userRepository.save(user);

            Account account = Account.builder()
                    .name("Nubank")
                    .balance(new BigDecimal("1000.00"))
                    .color("#8A05BE")
                    .user(user)
                    .build();
            accountRepository.save(account);

            Category category = Category.builder()
                    .name("Alimentação")
                    .icon("fast-food")
                    .color("#FF5733")
                    .build();
            categoryRepository.save(category);

            System.out.println("--- DADOS DE TESTE CARREGADOS PELA PRIMEIRA VEZ ---");
        } else {
            System.out.println("--- DADOS DE TESTE JÁ EXISTEM NO BANCO. PULANDO CARGA... ---");
        }
    }
}
