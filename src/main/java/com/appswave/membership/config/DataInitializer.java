package com.appswave.membership.config;

import com.appswave.membership.entity.User;
import com.appswave.membership.enums.Role;
import com.appswave.membership.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@appswave.com")) {
            User admin = User.builder()
                    .email("admin@appswave.com")
                    .password(passwordEncoder.encode("Admin@2026"))
                    .firstName("Admin")
                    .lastName("Appswave")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Admin user created: admin@appswave.com / Admin@2026");
        }
    }
}
