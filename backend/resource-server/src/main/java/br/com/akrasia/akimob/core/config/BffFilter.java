package br.com.akrasia.akimob.core.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BffFilter extends OncePerRequestFilter {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("Access Token {}", getAuthorizedClient(request, response));
        filterChain.doFilter(request, response);
    }

    private String getAuthorizedClient(HttpServletRequest request, HttpServletResponse response) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("akimob")
                .principal(principal)
                .attribute(HttpServletRequest.class.getName(), request)
                .attribute(HttpServletResponse.class.getName(), response)
                .build();

        return authorizedClientManager.authorize(authorizeRequest).getAccessToken().getTokenValue();
    }

}