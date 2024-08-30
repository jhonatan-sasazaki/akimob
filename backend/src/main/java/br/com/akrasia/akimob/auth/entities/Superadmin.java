package br.com.akrasia.akimob.auth.entities;

import br.com.akrasia.akimob.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Superadmin {

    @Id
    @Column(name = "user_account_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_account_id")
    private User user;

    public Superadmin(User user) {
        this.id = user.getId();
        this.user = user;
    }

}
