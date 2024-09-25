package br.com.akrasia.akimob.core.authentication.token;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenService {

    private final Algorithm algorithm;

    private final long TOKEN_EXPIRATION_TIME = 2 * 60 * 60; // 2 hours

    public TokenService(@Value("${akimob.jwt.secret}") String secret) {
        algorithm = Algorithm.HMAC256(secret);
    }

    public Token createToken(UserDetails user) {

        log.info("Creating token for user: {}", user.getUsername());

        try {
            Instant expiresAt = createExpirationTime();
            String token = JWT.create()
                    .withIssuer("akimob")
                    .withSubject(user.getUsername())
                    .withExpiresAt(expiresAt)
                    .sign(algorithm);

            log.info("Token created successfully for user: {}", user.getUsername());

            return new Token(user.getUsername(), expiresAt, token);

        } catch (JWTCreationException e) {
            log.error("Error creating JWT token, {}", e.getMessage());
            throw new RuntimeException("Error creating JWT token", e);
        }
    }

    public String validateToken(String token) {

        log.info("Validating token.");

        try {
            String subject = JWT
                    .require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();

            log.info("Token validated successfully for subject: {}", subject);
            return subject;

        } catch (JWTVerificationException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return null;
        }
    }

    public String extractUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    private Instant createExpirationTime() {
        return Instant.now().plusSeconds(TOKEN_EXPIRATION_TIME);
    }

}
