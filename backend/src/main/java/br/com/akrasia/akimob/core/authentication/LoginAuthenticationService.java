package br.com.akrasia.akimob.core.authentication;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationDTO;
import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationResponseDTO;
import br.com.akrasia.akimob.core.authentication.token.Token;
import br.com.akrasia.akimob.core.authentication.token.TokenService;
import br.com.akrasia.akimob.core.client.Client;
import br.com.akrasia.akimob.core.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationResponseDTO authenticate(AuthenticationDTO authenticationDTO) {
        log.info("Authenticating user: {}", authenticationDTO.username());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.username(), authenticationDTO.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        log.info("Authentication successful for user: {}", authenticationDTO.username());
        User user = (User) authentication.getPrincipal();
        Token token = tokenService.createToken(user);
        Set<String> clients = user.getClients().stream().map(Client::getSchemaName).collect(Collectors.toSet());

        return new AuthenticationResponseDTO(token, clients);
    }

}
