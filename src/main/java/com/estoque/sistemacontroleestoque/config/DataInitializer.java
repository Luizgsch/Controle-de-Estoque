package com.estoque.sistemacontroleestoque.config;

import com.estoque.sistemacontroleestoque.model.Usuario;
import com.estoque.sistemacontroleestoque.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            var admin = Usuario.builder()
                    .nome("Administrador")
                    .email("admin@estoque.com")
                    .senha(passwordEncoder.encode("admin123"))
                    .build();

            usuarioRepository.save(admin);
            log.info("Usuário padrão criado: admin@estoque.com / admin123");
        }
    }
}
