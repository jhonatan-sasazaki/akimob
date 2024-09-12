package br.com.akrasia.akimob.person;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.person.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
