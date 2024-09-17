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
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Locality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locality_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "administrative_area_id")
    private AdministrativeArea administrativeArea;

    private String name;
    
    @OneToMany(mappedBy = "locality")
    private Set<Street> streets;
}
