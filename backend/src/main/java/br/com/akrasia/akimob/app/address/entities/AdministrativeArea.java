package br.com.akrasia.akimob.app.address.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "administrative_area")
public class AdministrativeArea {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrative_area_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    private String name;

    @OneToMany(mappedBy = "administrativeArea")
    private Set<Locality> localities;

}
