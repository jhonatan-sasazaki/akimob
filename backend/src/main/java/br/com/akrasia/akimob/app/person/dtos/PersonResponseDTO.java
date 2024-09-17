package br.com.akrasia.akimob.app.person.dtos;

import br.com.akrasia.akimob.app.person.entities.Person;

public record PersonResponseDTO(Long id) {

    public PersonResponseDTO(Person person) {
        this(person.getId());
    }

}
