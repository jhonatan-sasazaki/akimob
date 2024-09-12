package br.com.akrasia.akimob.person.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LegalEntity extends Person {

    private String businessIdentifier; // CNPJ
    private String registeredName;

}
