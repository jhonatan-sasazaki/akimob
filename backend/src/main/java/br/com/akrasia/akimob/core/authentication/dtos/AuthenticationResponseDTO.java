package br.com.akrasia.akimob.core.authentication.dtos;

import java.util.Set;

import br.com.akrasia.akimob.core.authentication.token.Token;

public record AuthenticationResponseDTO(Token token, Set<String> clients) {
}
