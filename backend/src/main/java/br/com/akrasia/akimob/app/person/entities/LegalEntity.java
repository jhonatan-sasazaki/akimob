package br.com.akrasia.akimob.app.person.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "legal_entity")
public class LegalEntity extends Person {

    @Column(name = "business_identifier")
    private String businessIdentifier; // CNPJ

    @Column(name = "registered_name")
    private String registeredName;

}
