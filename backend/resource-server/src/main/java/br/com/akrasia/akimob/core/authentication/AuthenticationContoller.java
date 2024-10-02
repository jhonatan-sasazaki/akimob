package br.com.akrasia.akimob.core.authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationDTO;
import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationContoller {

    private final LoginAuthenticationService loginAuthenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        log.info("Login request received for user: {}", authenticationDTO.username());

        AuthenticationResponseDTO responseDTO = loginAuthenticationService.authenticate(authenticationDTO);
        return ResponseEntity.ok(responseDTO);
    }

}
