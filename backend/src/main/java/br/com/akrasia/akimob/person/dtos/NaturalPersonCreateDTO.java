package br.com.akrasia.akimob.person.dtos;

import java.time.LocalDate;

import br.com.akrasia.akimob.person.entities.NaturalPerson.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NaturalPersonCreateDTO {

    private String personalIdentifier;
    private Gender gender;
    private LocalDate birthdate;
}
