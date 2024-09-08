package br.com.akrasia.akimob.superadmin;

import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.client.ClientService;
import br.com.akrasia.akimob.client.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.client.dtos.ClientResponseDTO;
import br.com.akrasia.akimob.user.UserService;
import br.com.akrasia.akimob.user.dtos.UserCreateDTO;
import br.com.akrasia.akimob.user.dtos.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class SuperadminController {

    private final ClientService clientService;
    private final UserService userService;

    @PostMapping("/clients")
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientCreateDTO clientCreateDTO) {
        ClientResponseDTO client = clientService.createClient(clientCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientResponseDTO>> listClients(@PageableDefault(size = 10) Pageable pageable) {
        List<ClientResponseDTO> clients = clientService.listClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        UserResponseDTO user = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
