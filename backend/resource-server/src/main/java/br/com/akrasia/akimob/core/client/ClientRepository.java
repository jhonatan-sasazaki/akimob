package br.com.akrasia.akimob.core.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.commons.core.client.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findAll(Pageable pageable);

}
