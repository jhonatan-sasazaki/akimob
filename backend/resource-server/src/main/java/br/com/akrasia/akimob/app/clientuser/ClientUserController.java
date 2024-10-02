package br.com.akrasia.akimob.app.clientuser;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.app.clientuser.dtos.ClientUserDTO;
import br.com.akrasia.akimob.core.authentication.ClientUserAuthenticationToken;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ClientUserController {

    private final ClientUserService clientUserService;

    @GetMapping("/me")
    public ResponseEntity<ClientUserDTO> getCurrentClientUser(Principal principal) {
        ClientUserDTO clientUserDTO = clientUserService.getClientUserDetails((ClientUserAuthenticationToken) principal);
        return ResponseEntity.ok(clientUserDTO);
    }

}
