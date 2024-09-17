package br.com.akrasia.akimob.app.person.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LegalEntityCreateDTO {

    private String businessIdentifier;
    private String registeredName;

}
