package br.com.akrasia.akimob.multiclient;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClientResolverFilter extends OncePerRequestFilter {

    private final ClientResolver clientResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Long clientId = clientResolver.resolveClientId(request);

        if (clientId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ClientContext.setClientId(clientId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            ClientContext.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Do not filter requests to superadmin and login endpoints
        return request.getRequestURI().startsWith("/superadmin") || request.getRequestURI().startsWith("/login");
    }
}
