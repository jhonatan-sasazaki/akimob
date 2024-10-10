package br.com.akrasia.akimob.authorization.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import br.com.akrasia.akimob.authorization.OAuthUser;

@Configuration(proxyBeanMethods = false)
public class JwtAuthoritiesCustomizer {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> {
                    OAuthUser oauthUser = (OAuthUser) context.getPrincipal().getPrincipal();

                    if (oauthUser.isSuperadmin()) {
                        claims.put("superadmin", true);
                    }

                    Map<String, Set<String>> authorities = new HashMap<>();
                    oauthUser.getClientsUsers().forEach((clientSchema, clientUser) -> {
                        Set<String> clientAuthorities = clientUser.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet());
                        authorities.put(clientSchema, clientAuthorities);
                    });
                    claims.put("authorities", authorities);
                });
            }
        };
    }

}
