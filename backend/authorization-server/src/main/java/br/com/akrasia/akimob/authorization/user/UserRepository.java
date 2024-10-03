package br.com.akrasia.akimob.authorization.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.commons.core.user.User;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);
    
}
