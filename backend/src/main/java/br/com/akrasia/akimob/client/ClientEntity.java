package br.com.akrasia.akimob.client;

import org.hibernate.annotations.TenantId;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ClientEntity {

    @TenantId
    @Column(name = "client_id")
    private Long clientId;
    
}
