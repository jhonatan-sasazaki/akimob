package br.com.akrasia.akimob.person.dtos;

import br.com.akrasia.akimob.person.entities.Person;

public record PersonResponseDTO(Long id) {

    public PersonResponseDTO(Person person) {
        this(person.getId());
    }

}
