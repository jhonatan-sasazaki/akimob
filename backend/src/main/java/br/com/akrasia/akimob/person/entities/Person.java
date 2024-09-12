package br.com.akrasia.akimob.person.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import br.com.akrasia.akimob.address.entities.Address;
import br.com.akrasia.akimob.client.ClientEntity;
import br.com.akrasia.akimob.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class Person extends ClientEntity {

    public static enum Type {
        NATURAL, LEGAL
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private Type type;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;

    private String phoneNumber;
    private String email;
    private String photoUrl;
    private String observation;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @CreatedBy
    @JoinColumn(name = "created_by")
    private User createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @LastModifiedBy
    @JoinColumn(name = "updated_by")
    private User updatedBy;

}
