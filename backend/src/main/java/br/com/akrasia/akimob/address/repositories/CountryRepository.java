package br.com.akrasia.akimob.address.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.address.entities.Country;

public interface CountryRepository extends JpaRepository<Country, Short> {
    
}
