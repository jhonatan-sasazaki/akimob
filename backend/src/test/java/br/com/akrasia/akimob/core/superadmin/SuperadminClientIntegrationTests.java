package br.com.akrasia.akimob.core.superadmin;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.akrasia.akimob.IntegrationTests;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.core.authentication.LoginAuthenticationService;
import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationDTO;
import br.com.akrasia.akimob.core.client.ClientRepository;
import br.com.akrasia.akimob.core.client.ClientService;
import br.com.akrasia.akimob.core.user.UserService;
import br.com.akrasia.akimob.core.user.dtos.UserCreateDTO;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Slf4j
public class SuperadminClientIntegrationTests extends IntegrationTests {

        @Autowired
        @Qualifier("client")
        private DataSource clientDataSource;

        @Autowired
        private WebTestClient webTestClient;

        @Autowired
        private ClientService clientService;

        @Autowired
        private ClientRepository clientRepository;

        @Autowired
        private LoginAuthenticationService loginAuthenticationService;

        @BeforeAll
        static void setUp(@Autowired UserService userService) {
                UserCreateDTO userCreateDTO = new UserCreateDTO("user", "password", "user@email.com");
                userService.createUser(userCreateDTO);
                UserCreateDTO superadminCreateDTO = new UserCreateDTO("superadmin", "password", "superadmin@email.com");
                userService.createSuperadmin(superadminCreateDTO);
        }

        @BeforeEach
        public void clearClient() throws SQLException {
                log.info("Clearing client table");

                clientRepository.deleteAll();
                clientRepository.flush();

                log.info("Clearing client database");

                JdbcTemplate jdbcTemplate = new JdbcTemplate(clientDataSource);
                String sql = "SELECT schema_name FROM information_schema.schemata WHERE (schema_name NOT LIKE 'pg_%') and (schema_name != 'information_schema');";
                jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("schema_name"))
                                .forEach(schemaName -> jdbcTemplate
                                                .execute("DROP SCHEMA IF EXISTS " + schemaName + " CASCADE"));
        }

        @Test
        public void createClient_Unauthenticated() throws Exception {
                ClientCreateDTO clientCreateDTO = new ClientCreateDTO("New Client", "newclient");

                webTestClient.post().uri("/superadmin/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(clientCreateDTO)
                                .exchange()
                                .expectStatus().isUnauthorized();

                assertTrue(clientRepository.findAll().isEmpty());
        }

        @Test
        public void createClient_AuthenticatedButNotAuthorized() throws Exception {
                String userToken = loginAuthenticationService.authenticate(new AuthenticationDTO("user", "password"))
                                .token().value();
                ClientCreateDTO clientCreateDTO = new ClientCreateDTO("New Client", "newclient");

                webTestClient.post().uri("/superadmin/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(http -> http.setBearerAuth(userToken))
                                .bodyValue(clientCreateDTO)
                                .exchange()
                                .expectStatus().isForbidden();

                assertTrue(clientRepository.findAll().isEmpty());
        }

        @Test
        public void createClient_Superadmin() throws Exception {
                String superadminToken = loginAuthenticationService
                                .authenticate(new AuthenticationDTO("superadmin", "password")).token().value();
                ClientCreateDTO clientCreateDTO = new ClientCreateDTO("New Client", "newclient");

                webTestClient.post().uri("/superadmin/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(http -> http.setBearerAuth(superadminToken))
                                .bodyValue(clientCreateDTO)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody()
                                .jsonPath("$.id").isNotEmpty()
                                .jsonPath("$.name").isEqualTo(clientCreateDTO.name());

                assertEquals(1, clientRepository.findAll().size());
        }

        @Test
        public void createClient_DuplicateName() throws Exception {
                String superadminToken = loginAuthenticationService
                                .authenticate(new AuthenticationDTO("superadmin", "password")).token().value();
                ClientCreateDTO clientCreateDTO = new ClientCreateDTO("New Client", "newclient");
                ClientCreateDTO clientCreateDTO2 = new ClientCreateDTO("New Client", "newclient2");

                clientService.createClient(clientCreateDTO);

                webTestClient.post().uri("/superadmin/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(http -> http.setBearerAuth(superadminToken))
                                .bodyValue(clientCreateDTO2)
                                .exchange()
                                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

                assertEquals(1, clientRepository.findAll().size());

        }

        @Test
        public void createClient_DuplicateSchemaName() throws Exception {
                String superadminToken = loginAuthenticationService
                                .authenticate(new AuthenticationDTO("superadmin", "password")).token().value();
                ClientCreateDTO clientCreateDTO = new ClientCreateDTO("New Client", "newclient");
                ClientCreateDTO clientCreateDTO2 = new ClientCreateDTO("New Client 2", "newclient");

                clientService.createClient(clientCreateDTO);

                webTestClient.post().uri("/superadmin/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(http -> http.setBearerAuth(superadminToken))
                                .bodyValue(clientCreateDTO2)
                                .exchange()
                                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

                assertEquals(1, clientRepository.findAll().size());
        }

}
