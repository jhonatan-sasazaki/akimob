package br.com.akrasia.akimob.app.rolegroup;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.akrasia.akimob.IntegrationTests;
import br.com.akrasia.akimob.app.clientuser.ClientUserRepository;
import br.com.akrasia.akimob.app.clientuser.ClientUserService;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserDTO;
import br.com.akrasia.akimob.commons.app.authority.Authority;
import br.com.akrasia.akimob.commons.app.authority.AuthorityService;
import br.com.akrasia.akimob.commons.app.rolegroup.RoleGroup;
import br.com.akrasia.akimob.commons.app.rolegroup.RoleGroupRepoository;
import br.com.akrasia.akimob.commons.app.rolegroup.RoleGroupService;
import br.com.akrasia.akimob.commons.app.rolegroup.dtos.RoleGroupCreateDTO;
import br.com.akrasia.akimob.commons.app.rolegroup.dtos.RoleGroupResponseDTO;
import br.com.akrasia.akimob.commons.core.client.context.ClientContext;
import br.com.akrasia.akimob.core.authentication.LoginAuthenticationService;
import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationDTO;
import br.com.akrasia.akimob.core.client.ClientService;
import br.com.akrasia.akimob.core.client.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.core.user.UserService;
import br.com.akrasia.akimob.core.user.dtos.UserCreateDTO;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Slf4j
public class RoleGroupIntegrationTests extends IntegrationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RoleGroupService roleGroupService;

    @Autowired
    private RoleGroupRepoository roleGroupRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private ClientUserRepository clientUserRepository;

    @Autowired
    private ClientUserService clientUserService;

    @Autowired
    private LoginAuthenticationService loginAuthenticationService;

    private static String headerName;

    private static String client1Id;
    private static String client2Id;

    private static Long user1Id;
    private static Long user2Id;

    private static AuthenticationDTO user1AuthenticationDTO;
    private static AuthenticationDTO user2AuthenticationDTO;

    @BeforeAll
    static void setUp(
            @Autowired ClientService clientService,
            @Autowired UserService userService,
            @Value("${akimob.multiclient.http.header-name}") String clientIdHeaderName) {

        headerName = clientIdHeaderName;

        client1Id = clientService.createClient(new ClientCreateDTO("Client 1", "client1")).schemaName();
        client2Id = clientService.createClient(new ClientCreateDTO("Client 2", "client2")).schemaName();

        user1Id = userService.createUser(new UserCreateDTO("user1", "password", "user1@email.com")).id();
        user2Id = userService.createUser(new UserCreateDTO("user2", "password", "user2@email.com")).id();

        user1AuthenticationDTO = new AuthenticationDTO("user1", "password");
        user2AuthenticationDTO = new AuthenticationDTO("user2", "password");
    }

    @BeforeEach
    void resetDatabase() {
        log.info("Resetting schema {}", client1Id);
        resetSchema(client1Id, user1Id);
        log.info("Resetting schema {}", client2Id);
        resetSchema(client2Id, user2Id);
    }

    @Test
    public void createRoleGroup_Unauthenticated() throws Exception {
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));

        webTestClient.post().uri("/rolegroups")
                .contentType(MediaType.APPLICATION_JSON)
                .header(headerName, client1Id)
                .bodyValue(roleGroupCreateDTO)
                .exchange()
                .expectStatus().isUnauthorized();

        ClientContext.setCurrentClient(client1Id);
        assertEquals(1, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void createRoleGroup_AuthenticatedButNotAuthorized() throws Exception {
        String userToken = loginAuthenticationService.authenticate(user2AuthenticationDTO).token().value();
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));

        webTestClient.post().uri("/rolegroups")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(roleGroupCreateDTO)
                .exchange()
                .expectStatus().isForbidden();

        ClientContext.setCurrentClient(client1Id);
        assertEquals(1, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void createRoleGroup_AuthenticatedAndAuthorized() throws Exception {
        String userToken = loginAuthenticationService.authenticate(user1AuthenticationDTO).token().value();
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));

        webTestClient.post().uri("/rolegroups")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(roleGroupCreateDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty();

        ClientContext.setCurrentClient(client1Id);
        assertEquals(2, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void createRoleGroup_DuplicateName() throws Exception {
        String userToken = loginAuthenticationService.authenticate(user1AuthenticationDTO).token().value();
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));
        RoleGroupCreateDTO roleGroupCreateDTO2 = new RoleGroupCreateDTO("Role Group 1", "Description 2",
                Set.of(1L, 2L));

        webTestClient.post().uri("/rolegroups")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(roleGroupCreateDTO)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post().uri("/rolegroups")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(roleGroupCreateDTO2)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        ClientContext.setCurrentClient(client1Id);
        assertEquals(2, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void createRoleGroup_DuplicateNameDifferentClients() throws Exception {
        String userToken = loginAuthenticationService.authenticate(user1AuthenticationDTO).token().value();
        String user2Token = loginAuthenticationService.authenticate(user2AuthenticationDTO).token().value();
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));
        RoleGroupCreateDTO roleGroupCreateDTO2 = new RoleGroupCreateDTO("Role Group 1", "Description 2",
                Set.of(1L, 2L));

        webTestClient.post().uri("/rolegroups")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(roleGroupCreateDTO)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post().uri("/rolegroups")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(user2Token);
                    http.add(headerName, client2Id);
                })
                .bodyValue(roleGroupCreateDTO2)
                .exchange()
                .expectStatus().isCreated();

        ClientContext.setCurrentClient(client1Id);
        assertEquals(2, roleGroupRepository.findAll().size());
        ClientContext.clear();

        ClientContext.setCurrentClient(client2Id);
        assertEquals(2, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void updateRoleGroup_Unauthenticated() throws Exception {
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));

        ClientContext.setCurrentClient(client1Id);
        RoleGroupResponseDTO roleGroup = roleGroupService.createRoleGroup(roleGroupCreateDTO);

        webTestClient.put().uri("/rolegroups/" + roleGroup.id())
                .contentType(MediaType.APPLICATION_JSON)
                .header(headerName, client1Id)
                .bodyValue(roleGroupCreateDTO)
                .exchange()
                .expectStatus().isUnauthorized();

        assertEquals(2, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void updateRoleGroup_AuthenticatedButNotAuthorized() throws Exception {
        String userToken = loginAuthenticationService.authenticate(user2AuthenticationDTO).token().value();
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));

        ClientContext.setCurrentClient(client1Id);
        RoleGroupResponseDTO roleGroup = roleGroupService.createRoleGroup(roleGroupCreateDTO);

        webTestClient.put().uri("/rolegroups/" + roleGroup.id())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(roleGroupCreateDTO)
                .exchange()
                .expectStatus().isForbidden();

        assertEquals(2, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void updateRoleGroup_AuthenticatedAndAuthorized() throws Exception {
        String userToken = loginAuthenticationService.authenticate(user1AuthenticationDTO).token().value();
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));

        ClientContext.setCurrentClient(client1Id);
        RoleGroupResponseDTO roleGroup = roleGroupService.createRoleGroup(roleGroupCreateDTO);

        RoleGroupCreateDTO updatedRoleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 2", "Description 2",
                Set.of(1L, 2L, 3L));

        webTestClient.put().uri("/rolegroups/" + roleGroup.id())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(updatedRoleGroupCreateDTO)
                .exchange()
                .expectStatus().isOk();

        RoleGroup updatedRoleGroup = roleGroupRepository.findById(roleGroup.id()).get();
        assertEquals("Role Group 2", updatedRoleGroup.getName());
        assertEquals("Description 2", updatedRoleGroup.getDescription());
        assertEquals(3, updatedRoleGroup.getAuthorities().size());
        assertEquals(2, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void updateRoleGroup_DuplicateName() throws Exception {
        String userToken = loginAuthenticationService.authenticate(user1AuthenticationDTO).token().value();
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));
        RoleGroupCreateDTO roleGroupCreateDTO2 = new RoleGroupCreateDTO("Role Group 2", "Description 2",
                Set.of(1L, 2L));

        ClientContext.setCurrentClient(client1Id);
        RoleGroupResponseDTO roleGroup = roleGroupService.createRoleGroup(roleGroupCreateDTO);
        roleGroupService.createRoleGroup(roleGroupCreateDTO2);

        RoleGroupCreateDTO updatedRoleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 2", "Description 2",
                Set.of(1L, 2L, 3L));

        webTestClient.put().uri("/rolegroups/" + roleGroup.id())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(updatedRoleGroupCreateDTO)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        RoleGroup updatedRoleGroup = roleGroupRepository.findById(roleGroup.id()).get();
        assertEquals("Role Group 1", updatedRoleGroup.getName());
        assertEquals("Description 1", updatedRoleGroup.getDescription());
        assertEquals(2, updatedRoleGroup.getAuthorities().size());
        assertEquals(3, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void updateRoleGroup_DuplicateNameDifferentClients() throws Exception {
        String userToken = loginAuthenticationService.authenticate(user1AuthenticationDTO).token().value();
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));
        RoleGroupCreateDTO roleGroupCreateDTO2 = new RoleGroupCreateDTO("Role Group 2", "Description 2",
                Set.of(1L, 2L));

        ClientContext.setCurrentClient(client1Id);
        RoleGroupResponseDTO roleGroup = roleGroupService.createRoleGroup(roleGroupCreateDTO);

        ClientContext.setCurrentClient(client2Id);
        roleGroupService.createRoleGroup(roleGroupCreateDTO2);

        RoleGroupCreateDTO updatedRoleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 2", "Description 2",
                Set.of(1L, 2L, 3L));

        webTestClient.put().uri("/rolegroups/" + roleGroup.id())
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(updatedRoleGroupCreateDTO)
                .exchange()
                .expectStatus().isOk();

        ClientContext.setCurrentClient(client1Id);
        RoleGroup updatedRoleGroup = roleGroupRepository.findById(roleGroup.id()).get();
        assertEquals("Role Group 2", updatedRoleGroup.getName());
        assertEquals("Description 2", updatedRoleGroup.getDescription());
        assertEquals(3, updatedRoleGroup.getAuthorities().size());
        assertEquals(2, roleGroupRepository.findAll().size());
        ClientContext.clear();

        ClientContext.setCurrentClient(client2Id);
        assertEquals(2, roleGroupRepository.findAll().size());
        ClientContext.clear();
    }

    @Test
    public void updateRoleGroup_NotFound() throws Exception {
        String userToken = loginAuthenticationService.authenticate(user1AuthenticationDTO).token().value();
        RoleGroupCreateDTO roleGroupCreateDTO = new RoleGroupCreateDTO("Role Group 1", "Description 1", Set.of(1L, 2L));

        webTestClient.put().uri("/rolegroups/999")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> {
                    http.setBearerAuth(userToken);
                    http.add(headerName, client1Id);
                })
                .bodyValue(roleGroupCreateDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    private void resetSchema(String schema, Long userId) {
        ClientContext.setCurrentClient(schema);

        clientUserRepository.deleteAll();
        clientUserRepository.flush();
        roleGroupRepository.deleteAll();
        roleGroupRepository.flush();

        Long roleGroupId = createTestRoleGroup();
        clientUserService.createClientUser(new ClientCreateUserDTO(userId, roleGroupId));

        ClientContext.clear();
    }

    private Long createTestRoleGroup() {
        String[] testAuthorities = new String[] { "role_group_add", "role_group_edit" };
        HashMap<String, Authority> authorityMap = authorityService.getAuthorityMap();

        Set<Authority> authorities = new HashSet<>();
        for (String authorityName : testAuthorities) {
            authorities.add(authorityMap.get(authorityName));
        }

        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setName("Test Role Group");
        roleGroup.setAuthorities(authorities);

        RoleGroup savedRoleGroup = roleGroupRepository.save(roleGroup);
        return savedRoleGroup.getId();
    }

}
