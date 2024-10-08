package br.com.akrasia.akimob.authorization;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.akrasia.akimob.commons.app.clientuser.ClientUser;
import br.com.akrasia.akimob.commons.core.user.User;

public class OAuthUser implements UserDetails {

    private User user;
    private ClientUser clientUser;

    public OAuthUser(User user) {
        this.user = user;
    }

    public OAuthUser(User user, ClientUser clientUser) {
        this.user = user;
        this.clientUser = clientUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (this.isSuperadmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SUPERADMIN"));
        }
        if (clientUser != null) {
            grantedAuthorities.addAll(clientUser.getAuthorities());
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    private boolean isSuperadmin() {
        return user.getSuperadmin() != null;
    }

}
