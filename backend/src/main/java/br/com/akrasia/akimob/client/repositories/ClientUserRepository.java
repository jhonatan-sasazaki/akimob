package br.com.akrasia.akimob.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.client.ClientUser;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {

}
