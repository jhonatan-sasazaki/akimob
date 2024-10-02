package br.com.akrasia.akimob.app.address.entities;

import java.util.Set;

import br.com.akrasia.akimob.app.person.entities.Person;
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
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "street_id")
    private Street street;

    @Column(name = "address_number")
    private String addressNumber;

    private String complement;

    @Column(name = "postal_code")
    private String postalCode;

    @OneToMany(mappedBy = "address")
    private Set<Person> people;

}
