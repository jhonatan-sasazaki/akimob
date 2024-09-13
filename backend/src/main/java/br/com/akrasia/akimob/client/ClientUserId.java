package br.com.akrasia.akimob.client;

import br.com.akrasia.akimob.user.User;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientUserId {

    private Client client;
    private User user;
    
}
