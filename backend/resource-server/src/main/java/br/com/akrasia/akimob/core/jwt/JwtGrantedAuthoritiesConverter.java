package br.com.akrasia.akimob.core.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import br.com.akrasia.akimob.commons.core.client.context.ClientContext;

public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : getAuthorities(jwt)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        return grantedAuthorities;
    }

    private Collection<String> getAuthorities(Jwt jwt) {
        Set<String> authorities = new HashSet<>();

        Boolean superadmin = jwt.getClaimAsBoolean("superadmin");
        if (superadmin) {
            authorities.add("ROLE_SUPERADMIN");
        }

        Map<String, Collection<String>> authoritiesClaim = jwt.getClaim("authorities");
        String currentClient = ClientContext.getCurrentClient();
        if (authoritiesClaim.containsKey(currentClient)) {
            for (String authority : authoritiesClaim.get(currentClient)) {
                authorities.add(authority);
            }
        }

        return authorities;
    }

}
