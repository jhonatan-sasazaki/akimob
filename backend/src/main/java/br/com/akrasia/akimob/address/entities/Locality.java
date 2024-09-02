package br.com.akrasia.akimob.address.entities;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import br.com.akrasia.akimob.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @CreationTimestamp
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
}
