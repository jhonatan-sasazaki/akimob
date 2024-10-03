package br.com.akrasia.akimob.commons.core.client.context;

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

        String client = clientResolver.resolveClient(request);

        ClientContext.setCurrentClient(client);

        try {
            filterChain.doFilter(request, response);
        } finally {
            ClientContext.clear();
        }
    }
}
