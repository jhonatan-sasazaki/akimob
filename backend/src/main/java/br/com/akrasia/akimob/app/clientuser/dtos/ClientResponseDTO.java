package br.com.akrasia.akimob.app.clientuser.dtos;

import br.com.akrasia.akimob.core.client.Client;

public record ClientResponseDTO(Long id, String name) {

    public ClientResponseDTO(Client client) {
        this(client.getId(), client.getName());
    }

}
