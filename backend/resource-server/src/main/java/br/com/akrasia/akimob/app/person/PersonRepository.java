package br.com.akrasia.akimob.app.person;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.akrasia.akimob.app.person.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
