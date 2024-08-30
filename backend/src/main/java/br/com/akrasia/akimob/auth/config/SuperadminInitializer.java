package br.com.akrasia.akimob.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.akrasia.akimob.auth.entities.Superadmin;
import br.com.akrasia.akimob.user.User;
import br.com.akrasia.akimob.user.UserRepository;

@Component
public class SuperadminInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${akimob.superadmin.username}")
    private String superAdminUsername;

    @Value("${akimob.superadmin.password}")
    private String superAdminPassword;

    @Value("${akimob.superadmin.email}")
    private String superAdminEmail;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findFirstBySuperadminNotNull().isEmpty()) {
            User superadmin = new User();
            superadmin.setUsername(superAdminUsername);
            superadmin.setPassword(passwordEncoder.encode(superAdminPassword));
            superadmin.setEmail(superAdminEmail);
            superadmin.setSuperadmin(new Superadmin(superadmin));
            userRepository.save(superadmin);
            System.out.println("Superadmin created");
        } else {
            // log superadmin already exists
            System.out.println("Superadmin already exists");
        }
    }
    
}
