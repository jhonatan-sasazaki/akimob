package br.com.akrasia.akimob.superadmin.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.client.ClientService;
import br.com.akrasia.akimob.client.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.client.dtos.ClientResponseDTO;
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

}
