package br.com.akrasia.akimob.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.auth.dtos.AuthenticationDTO;
import br.com.akrasia.akimob.auth.dtos.AuthenticationResponseDTO;
import br.com.akrasia.akimob.auth.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationContoller {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        log.info("Login request received for user: {}", authenticationDTO.username());
        
        String token = authenticationService.authenticate(authenticationDTO);
        return ResponseEntity.ok(new AuthenticationResponseDTO(token));
    }
    
}
