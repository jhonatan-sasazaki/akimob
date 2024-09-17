package br.com.akrasia.akimob.core.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationDTO;
import br.com.akrasia.akimob.core.authentication.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public String authenticate(AuthenticationDTO authenticationDTO) {
        log.info("Authenticating user: {}", authenticationDTO.username());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.username(), authenticationDTO.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        log.info("Authentication successful for user: {}", authenticationDTO.username());
        return tokenService.createToken((UserDetails) authentication.getPrincipal());
    }

}
