package br.com.akrasia.akimob.core.client.dtos;

import br.com.akrasia.akimob.commons.core.client.Client;

public record ClientResponseDTO(Long id, String name, String schemaName) {

    public ClientResponseDTO(Client client) {
        this(client.getId(), client.getName(), client.getSchemaName());
    }

}
