package br.com.akrasia.akimob.app.address.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Country {

    @Id
    @Column(name = "country_id")
    private short id;

    private String iso2;

    private String name;

    @OneToMany(mappedBy = "country")
    private Set<AdministrativeArea> administrativeAreas;
    
}
