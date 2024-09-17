package br.com.akrasia.akimob.app.address.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.app.address.entities.Street;

public interface StreetRepository extends JpaRepository<Street, Long> {

}
