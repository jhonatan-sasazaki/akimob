package br.com.akrasia.akimob.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.akrasia.akimob.superadmin.Superadmin;
import br.com.akrasia.akimob.user.User;
import br.com.akrasia.akimob.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuperadminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${akimob.superadmin.username}")
    private String superAdminUsername;

    @Value("${akimob.superadmin.password}")
    private String superAdminPassword;

    @Value("${akimob.superadmin.email}")
    private String superAdminEmail;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findFirstBySuperadminNotNull().isEmpty()) {
            log.info("Superadmin not found, creating default one");

            User superadmin = new User();
            superadmin.setUsername(superAdminUsername);
            superadmin.setPassword(passwordEncoder.encode(superAdminPassword));
            superadmin.setEmail(superAdminEmail);
            superadmin.setSuperadmin(new Superadmin(superadmin));
            userRepository.save(superadmin);

            log.info("Superadmin created");

        } else {
            log.info("Superadmin found, skipping creation");
        }
    }

}
