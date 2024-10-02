package br.com.akrasia.akimob.core.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import br.com.akrasia.akimob.app.clientuser.ClientUser;
import br.com.akrasia.akimob.core.user.User;

public class ClientUserAuthenticationToken implements Authentication {

    private final User user;
    private final ClientUser clientUser;
    private final Collection<? extends GrantedAuthority> authorities;

    public ClientUserAuthenticationToken(User user, ClientUser clientUser) {
        this.user = user;
        this.clientUser = clientUser;
        this.authorities = setAuthorities();
    }

    private Collection<? extends GrantedAuthority> setAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(user.getAuthorities());
        if (Objects.nonNull(clientUser)) {
            authorities.addAll(clientUser.getAuthorities());
        }

        return Collections.unmodifiableList(new ArrayList<>(authorities));

    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return clientUser;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new RuntimeException("Not allowed to change authentication status");
    }

}
