package br.com.akrasia.akimob.client.dtos;

import br.com.akrasia.akimob.client.Client;

public record ClientResponseDTO(Long id, String name) {

    public ClientResponseDTO(Client client) {
        this(client.getId(), client.getName());
    }

}
