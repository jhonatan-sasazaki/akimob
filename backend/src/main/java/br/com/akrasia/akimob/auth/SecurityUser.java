package br.com.akrasia.akimob.auth;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.akrasia.akimob.auth.authority.Authority;
import br.com.akrasia.akimob.user.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private User user;

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Authority> authorities = getUserAuthorities();
        Set<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(toSet());
        if (isSuperadmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SUPERADMIN"));
        }

        return grantedAuthorities;
    }

    private Set<Authority> getUserAuthorities() {
        return user.getRoleGroups().stream()
                .flatMap(roleGroup -> roleGroup.getAuthorities().stream())
                .collect(toSet());
    }

    private boolean isSuperadmin() {
        return user.getSuperadmin() != null;
    }

}
