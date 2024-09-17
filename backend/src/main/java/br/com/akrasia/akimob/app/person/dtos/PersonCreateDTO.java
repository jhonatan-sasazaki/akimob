package br.com.akrasia.akimob.app.person.dtos;

import br.com.akrasia.akimob.app.address.dtos.AddressCreateDTO;
import br.com.akrasia.akimob.app.person.entities.Person.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PersonCreateDTO {

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    private Type type; // NATURAL or LEGAL

    private AddressCreateDTO address;
    private String phoneNumber;
    private String email;
    private String observation;

    // For NATUAL type
    private NaturalPersonCreateDTO naturalPerson;

    // For LEGAL type
    private LegalEntityCreateDTO legalEntity;

}
