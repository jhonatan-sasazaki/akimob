package br.com.akrasia.akimob.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import br.com.akrasia.akimob.user.User;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
    
}
