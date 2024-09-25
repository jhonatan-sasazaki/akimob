package br.com.akrasia.akimob.core.authentication.dtos;

import br.com.akrasia.akimob.core.authentication.token.Token;

public record AuthenticationResponseDTO(Token token) {
}
