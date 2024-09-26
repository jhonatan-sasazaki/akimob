package br.com.akrasia.akimob.core.authentication.token;

public record Token(String subject, Long expiresAt, String value) {

}
