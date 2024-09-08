package br.com.akrasia.akimob.superadmin;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.akrasia.akimob.IntegrationTests;
import br.com.akrasia.akimob.auth.dtos.AuthenticationDTO;
import br.com.akrasia.akimob.auth.services.AuthenticationService;
import br.com.akrasia.akimob.client.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.user.UserService;
import br.com.akrasia.akimob.user.dtos.UserCreateDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class SuperadminClientIntegrationTests extends IntegrationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AuthenticationService authenticationService;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(@Autowired UserService userService) {
        UserCreateDTO userCreateDTO = new UserCreateDTO("user", "password", "user@email.com");
        userService.createUser(userCreateDTO);
        UserCreateDTO superadminCreateDTO = new UserCreateDTO("superadmin", "password", "superadmin@email.com");
        userService.createSuperadmin(superadminCreateDTO);

        objectMapper = new ObjectMapper();
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

    @Test
    public void createClient_Unauthenticated() throws Exception {
        ClientCreateDTO clientCreateDTO = new ClientCreateDTO("New Client");

        webTestClient.post().uri("/superadmin/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(clientCreateDTO))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void createClient_AuthenticatedButNotAuthorized() throws Exception {
        String userToken = authenticationService.authenticate(new AuthenticationDTO("user", "password"));
        ClientCreateDTO clientCreateDTO = new ClientCreateDTO("New Client");

        webTestClient.post().uri("/superadmin/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> http.setBearerAuth(userToken))
                .bodyValue(objectMapper.writeValueAsString(clientCreateDTO))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void createClient_Superadmin() throws Exception {
        String superadminToken = authenticationService.authenticate(new AuthenticationDTO("superadmin", "password"));
        ClientCreateDTO clientCreateDTO = new ClientCreateDTO("New Client");

        webTestClient.post().uri("/superadmin/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> http.setBearerAuth(superadminToken))
                .bodyValue(objectMapper.writeValueAsString(clientCreateDTO))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo(clientCreateDTO.name());
    }

}
