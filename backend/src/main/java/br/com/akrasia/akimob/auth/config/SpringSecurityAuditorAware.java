package br.com.akrasia.akimob.auth.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import br.com.akrasia.akimob.auth.SecurityUser;
import br.com.akrasia.akimob.user.User;

public class SpringSecurityAuditorAware implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .map(principal -> ((SecurityUser) principal).getUser());
    }
}
