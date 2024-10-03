package br.com.akrasia.akimob.commons.core.superadmin;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import br.com.akrasia.akimob.commons.core.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Superadmin {

    @Id
    @Column(name = "users_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "users_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at")
    private Instant createdAt;

    public Superadmin(User user) {
        this.id = user.getId();
        this.user = user;
    }

}
