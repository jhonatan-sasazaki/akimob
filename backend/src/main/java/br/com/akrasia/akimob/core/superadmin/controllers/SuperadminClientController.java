package br.com.akrasia.akimob.core.superadmin.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserDTO;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserResponseDTO;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientResponseDTO;
import br.com.akrasia.akimob.core.client.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/superadmin/clients")
@RequiredArgsConstructor
public class SuperadminClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientCreateDTO clientCreateDTO) {
        ClientResponseDTO client = clientService.createClient(clientCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @GetMapping
    public ResponseEntity<PagedModel<ClientResponseDTO>> listClients(@PageableDefault(size = 10) Pageable pageable) {
        PagedModel<ClientResponseDTO> clients = clientService.listClients(pageable);
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/{clientId}/users")
    public ResponseEntity<ClientCreateUserResponseDTO> createClientUser(@PathVariable Long clientId,
            @RequestBody @Valid ClientCreateUserDTO clientCreateUserDTO) {

        ClientCreateUserResponseDTO user = clientService.createClientUser(clientId, clientCreateUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
