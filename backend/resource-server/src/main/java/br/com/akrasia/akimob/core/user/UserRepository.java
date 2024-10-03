package br.com.akrasia.akimob.core.user;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.commons.core.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);

    Optional<User> findFirstBySuperadminNotNull();
    
}
