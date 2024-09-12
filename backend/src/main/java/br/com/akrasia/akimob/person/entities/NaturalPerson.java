package br.com.akrasia.akimob.person.entities;

import java.time.LocalDate;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NaturalPerson extends Person {

    public static enum Gender {
        MALE, FEMALE, OTHER
    };

    private String personalIdentifier; // CPF

    @Enumerated(EnumType.STRING)
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private Gender gender;
    
    private LocalDate birthdate;
}
