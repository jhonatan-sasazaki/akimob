package br.com.akrasia.akimob.address.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.address.entities.AdministrativeArea;

public interface AdministrativeAreaRepository extends JpaRepository<AdministrativeArea, Integer> {

}
