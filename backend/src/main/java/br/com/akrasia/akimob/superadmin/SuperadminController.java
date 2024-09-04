package br.com.akrasia.akimob.superadmin;

import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.client.Client;
import br.com.akrasia.akimob.client.ClientService;
import br.com.akrasia.akimob.client.dtos.ClientCreateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/superadmin")
@RequiredArgsConstructor
@Slf4j
public class SuperadminController {

    private final ClientService clientService;

    @PostMapping("/client")
    public ResponseEntity<Client> createClient(@RequestBody @Valid ClientCreateDTO clientCreateDTO) {
        Client client = clientService.createClient(clientCreateDTO);
        return ResponseEntity.ok(client);
    }
}
