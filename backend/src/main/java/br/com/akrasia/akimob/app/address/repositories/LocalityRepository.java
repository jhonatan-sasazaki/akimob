package br.com.akrasia.akimob.app.address.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.app.address.entities.Locality;

public interface LocalityRepository extends JpaRepository<Locality, Integer> {

}
