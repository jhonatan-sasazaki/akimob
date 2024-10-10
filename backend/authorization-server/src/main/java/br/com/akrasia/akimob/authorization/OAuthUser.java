package br.com.akrasia.akimob.authorization;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.akrasia.akimob.commons.app.clientuser.ClientUser;
import br.com.akrasia.akimob.commons.core.user.User;

public class OAuthUser implements UserDetails {

    private User user;
    // Map<ClientSchema, ClientUser>
    private Map<String, ClientUser> clientsUsers;

    public OAuthUser(User user) {
        this.user = user;
    }

    public OAuthUser(User user, Map<String, ClientUser> clientsUsers) {
        this.user = user;
        this.clientsUsers = clientsUsers;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public boolean isSuperadmin() {
        return user.getSuperadmin() != null;
    }

    public Map<String, ClientUser> getClientsUsers() {
        return clientsUsers;
    }

}
