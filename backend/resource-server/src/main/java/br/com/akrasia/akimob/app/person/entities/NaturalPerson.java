package br.com.akrasia.akimob.app.person.entities;

import java.time.LocalDate;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "natural_person")
public class NaturalPerson extends Person {

    public static enum Gender {
        MALE, FEMALE, OTHER
    };

    @Column(name = "personal_identifier")
    private String personalIdentifier; // CPF

    @Enumerated(EnumType.STRING)
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private Gender gender;
    
    private LocalDate birthdate;
}
