package br.com.akrasia.akimob.core.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BffFilter extends OncePerRequestFilter {

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final RestClient restClient;
    private final String resourceUrl;

    public BffFilter(OAuth2AuthorizedClientManager authorizedClientManager,
            @Value("${akimob.resourceserver.url}") String resourceUrl) {
        this.authorizedClientManager = authorizedClientManager;
        this.restClient = RestClient.create();
        this.resourceUrl = resourceUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        forward(request, response);
    }

    public void forward(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        String uri = resourceUrl + request.getRequestURI();
        String bearer = "Bearer " + getAuthorizedClient(request, response);

        ResponseEntity<String> responseEntity = restClient.method(method)
                .uri(uri)
                .header("Authorization", bearer)
                .retrieve()
                .toEntity(String.class);

        response.setStatus(responseEntity.getStatusCode().value());

        responseEntity.getHeaders().forEach((key, value) -> response.setHeader(key, value.get(0)));
        response.getWriter().write(responseEntity.getBody());
        response.getWriter().flush();
        response.getWriter().close();

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