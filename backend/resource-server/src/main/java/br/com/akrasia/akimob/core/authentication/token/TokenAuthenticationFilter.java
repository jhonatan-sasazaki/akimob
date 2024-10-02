package br.com.akrasia.akimob.core.authentication.token;

import java.io.IOException;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.akrasia.akimob.app.clientuser.ClientUser;
import br.com.akrasia.akimob.app.clientuser.ClientUserService;
import br.com.akrasia.akimob.core.authentication.AuthenticationService;
import br.com.akrasia.akimob.core.authentication.ClientUserAuthenticationToken;
import br.com.akrasia.akimob.core.authentication.CustomAuthenticationEntryPoint;
import br.com.akrasia.akimob.core.client.context.ClientContext;
import br.com.akrasia.akimob.core.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthenticationService authenticationService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final ClientUserService clientUserService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);
        if (token == null) {
            log.debug("No Bearer token found continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        String username = tokenService.validateToken(token);
        if (username == null) {
            log.info("Invalid token, UNAUTHORIZED");
            customAuthenticationEntryPoint.commence(request, response, null);
            return;
        }

        UserDetails user = authenticationService.loadUserByUsername(username);
        ClientUser clientUser = getClientUser(user);

        ClientUserAuthenticationToken authentication = new ClientUserAuthenticationToken((User) user, clientUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7);
    }

    private ClientUser getClientUser(UserDetails user) {
        
        if (Objects.isNull(ClientContext.getCurrentClient())) {
            log.debug("No client context found, skipping client user lookup");
            return null;
        }

        try {
            return clientUserService.getClientUser(((User) user).getId());
        } catch (Exception e) {
            log.warn("Error getting client user for {}", user.getUsername());
            return null;
        }
    }

}
