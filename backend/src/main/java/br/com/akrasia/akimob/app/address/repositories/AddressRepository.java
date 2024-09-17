package br.com.akrasia.akimob.app.address.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.app.address.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
