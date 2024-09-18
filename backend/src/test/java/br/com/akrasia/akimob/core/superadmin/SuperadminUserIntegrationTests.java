package br.com.akrasia.akimob.core.superadmin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.akrasia.akimob.IntegrationTests;
import br.com.akrasia.akimob.core.authentication.LoginAuthenticationService;
import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationDTO;
import br.com.akrasia.akimob.core.user.UserService;
import br.com.akrasia.akimob.core.user.dtos.UserCreateDTO;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Slf4j
public class SuperadminUserIntegrationTests extends IntegrationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private LoginAuthenticationService loginAuthenticationService;

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
    public void createUser_Superadmin() throws Exception {
        String superadminToken = loginAuthenticationService.authenticate(new AuthenticationDTO("superadmin", "password"));
        UserCreateDTO userCreateDTO = new UserCreateDTO("newuser", "password", "newuser@email.com");

        webTestClient.post().uri("/superadmin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> http.setBearerAuth(superadminToken))
                .bodyValue(objectMapper.writeValueAsString(userCreateDTO))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.username").isEqualTo(userCreateDTO.username())
                .jsonPath("$.email").isEqualTo(userCreateDTO.email());
    }

}
