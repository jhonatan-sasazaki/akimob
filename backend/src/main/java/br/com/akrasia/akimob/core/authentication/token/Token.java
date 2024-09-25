package br.com.akrasia.akimob.core.authentication.token;

import java.time.Instant;

public record Token(String subject, Instant expiresAt, String value) {

}
