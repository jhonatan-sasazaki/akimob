package br.com.akrasia.akimob.core.authentication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.akrasia.akimob.IntegrationTests;
import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationDTO;
import br.com.akrasia.akimob.core.user.UserService;
import br.com.akrasia.akimob.core.user.dtos.UserCreateDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthenticationIntegrationTests extends IntegrationTests {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    static void setUp(@Autowired UserService userService) {
        UserCreateDTO userCreateDTO = new UserCreateDTO("user", "password", "user@email.com");
        userService.createUser(userCreateDTO);
    }

    @Test
    public void login_ValidCredentials() {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AuthenticationDTO("user", "password"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.token").isNotEmpty();

    }

    @Test
    public void login_InvalidCredentials() {
        webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AuthenticationDTO("user", "invalid"))
                .exchange()
                .expectStatus().isUnauthorized();
    }

}
