package br.com.akrasia.akimob.address.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.address.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
