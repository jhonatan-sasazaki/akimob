package br.com.akrasia.akimob.client;

import br.com.akrasia.akimob.auth.rolegroup.RoleGroup;
import br.com.akrasia.akimob.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(ClientUserId.class)
@Table(name = "client_user_account")
public class ClientUser {

    @Id
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_group_id")
    private RoleGroup roleGroup;

}
