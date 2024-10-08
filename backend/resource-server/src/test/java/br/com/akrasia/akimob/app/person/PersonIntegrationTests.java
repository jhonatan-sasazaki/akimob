// package br.com.akrasia.akimob.app.person;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.Set;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.reactive.server.WebTestClient;

// import br.com.akrasia.akimob.IntegrationTests;
// import br.com.akrasia.akimob.app.clientuser.ClientUserService;
// import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserDTO;
// import br.com.akrasia.akimob.app.person.dtos.PersonCreateDTO;
// import br.com.akrasia.akimob.app.person.entities.Person.Type;
// import br.com.akrasia.akimob.commons.app.authority.Authority;
// import br.com.akrasia.akimob.commons.app.authority.AuthorityService;
// import br.com.akrasia.akimob.commons.app.rolegroup.RoleGroup;
// import br.com.akrasia.akimob.commons.app.rolegroup.RoleGroupRepoository;
// import br.com.akrasia.akimob.commons.core.client.context.ClientContext;
// import br.com.akrasia.akimob.core.authentication.LoginAuthenticationService;
// import br.com.akrasia.akimob.core.authentication.dtos.AuthenticationDTO;
// import br.com.akrasia.akimob.core.client.ClientService;
// import br.com.akrasia.akimob.core.client.dtos.ClientCreateDTO;
// import br.com.akrasia.akimob.core.user.UserService;
// import br.com.akrasia.akimob.core.user.dtos.UserCreateDTO;
// import lombok.extern.slf4j.Slf4j;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @AutoConfigureWebTestClient
// @Slf4j
// public class PersonIntegrationTests extends IntegrationTests {

//     @Autowired
//     private WebTestClient webTestClient;

//     @Autowired
//     private PersonRepository personRepository;

//     @Autowired
//     private LoginAuthenticationService loginAuthenticationService;

//     private static String headerName;

//     private static String client1Id;
//     private static String client2Id;

//     private static Long user1Id;
//     private static Long user2Id;

//     private static AuthenticationDTO user1AuthenticationDTO;
//     private static AuthenticationDTO user2AuthenticationDTO;

//     @BeforeAll
//     static void setUp(
//             @Autowired ClientService clientService,
//             @Autowired UserService userService,
//             @Autowired AuthorityService authorityService,
//             @Autowired RoleGroupRepoository roleGroupRepository,
//             @Autowired ClientUserService clientUserService,
//             @Value("${akimob.multiclient.http.header-name}") String clientIdHeaderName) {

//         headerName = clientIdHeaderName;

//         client1Id = clientService.createClient(new ClientCreateDTO("Client 1", "client1")).schemaName();
//         client2Id = clientService.createClient(new ClientCreateDTO("Client 2", "client2")).schemaName();

//         user1Id = userService.createUser(new UserCreateDTO("user1", "password", "user1@email.com")).id();
//         user2Id = userService.createUser(new UserCreateDTO("user2", "password", "user2@email.com")).id();

//         user1AuthenticationDTO = new AuthenticationDTO("user1", "password");
//         user2AuthenticationDTO = new AuthenticationDTO("user2", "password");

//         ClientContext.setCurrentClient(client1Id);
//         Long roleGroup1Id = createTestRoleGroup(roleGroupRepository, authorityService);
//         clientUserService.createClientUser(new ClientCreateUserDTO(user1Id, roleGroup1Id));

//         ClientContext.setCurrentClient(client2Id);
//         Long roleGroup2Id = createTestRoleGroup(roleGroupRepository, authorityService);
//         clientUserService.createClientUser(new ClientCreateUserDTO(user2Id, roleGroup2Id));

//     }

//     @BeforeEach
//     void clearTables() {
//         log.info("Clearing person table from schema {}", client1Id);
//         clearTable(client1Id);
//         log.info("Clearing person table from schema {}", client2Id);
//         clearTable(client2Id);
//     }

//     @Test
//     public void createPerson_Unauthenticated() {
//         PersonCreateDTO personCreateDTO = getPersonCreateDTO("Person 1");

//         webTestClient.post().uri("/people")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .header(headerName, client1Id)
//                 .bodyValue(personCreateDTO)
//                 .exchange()
//                 .expectStatus().isUnauthorized();

//         ClientContext.setCurrentClient(client1Id);
//         assertTrue(personRepository.findAll().isEmpty());
//         ClientContext.clear();
//     }

//     @Test
//     public void createPerson_AuthenticatedButNotAuthorized() {
//         String userToken = loginAuthenticationService.authenticate(user2AuthenticationDTO).token().value();
//         PersonCreateDTO personCreateDTO = getPersonCreateDTO("Person 1");

//         webTestClient.post().uri("/people")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .headers(http -> {
//                     http.setBearerAuth(userToken);
//                     http.add(headerName, client1Id);
//                 })
//                 .bodyValue(personCreateDTO)
//                 .exchange()
//                 .expectStatus().isForbidden();

//         ClientContext.setCurrentClient(client1Id);
//         assertTrue(personRepository.findAll().isEmpty());
//         ClientContext.clear();
//     }

//     @Test
//     public void createPerson_AuthenticatedAndAuthorized() {
//         String userToken = loginAuthenticationService.authenticate(user1AuthenticationDTO).token().value();
//         PersonCreateDTO personCreateDTO = getPersonCreateDTO("Person 1");

//         webTestClient.post().uri("/people")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .headers(http -> {
//                     http.setBearerAuth(userToken);
//                     http.add(headerName, client1Id);
//                 })
//                 .bodyValue(personCreateDTO)
//                 .exchange()
//                 .expectStatus().isCreated()
//                 .expectBody()
//                 .jsonPath("$.id").isNotEmpty();

//         ClientContext.setCurrentClient(client1Id);
//         assertEquals(1, personRepository.findAll().size());
//         ClientContext.clear();
//     }

//     @Test
//     public void createPerson_InvalidType() {
//         String userToken = loginAuthenticationService.authenticate(user1AuthenticationDTO).token().value();

//         webTestClient.post().uri("/people")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .headers(http -> {
//                     http.setBearerAuth(userToken);
//                     http.add(headerName, client1Id);
//                 })
//                 .bodyValue("{\"name\":\"Person 1\",\"type\":\"INVALID\"}")
//                 .exchange()
//                 .expectStatus().isBadRequest();

//         ClientContext.setCurrentClient(client1Id);
//         assertTrue(personRepository.findAll().isEmpty());
//         ClientContext.clear();
//     }

//     private PersonCreateDTO getPersonCreateDTO(String name) {
//         return new PersonCreateDTO(name, Type.NATURAL, null, null, null, null, null, null);
//     }

//     private static Long createTestRoleGroup(RoleGroupRepoository roleGroupRepository,
//             AuthorityService authorityService) {

//         String[] testAuthorities = new String[] { "person_add" };
//         HashMap<String, Authority> authorityMap = authorityService.getAuthorityMap();

//         Set<Authority> authorities = new HashSet<>();
//         for (String authorityName : testAuthorities) {
//             authorities.add(authorityMap.get(authorityName));
//         }

//         RoleGroup roleGroup = new RoleGroup();
//         roleGroup.setName("Test Role Group");
//         roleGroup.setAuthorities(authorities);

//         RoleGroup savedRoleGroup = roleGroupRepository.save(roleGroup);
//         return savedRoleGroup.getId();
//     }

//     private void clearTable(String schema) {
//         ClientContext.setCurrentClient(schema);

//         personRepository.deleteAll();
//         personRepository.flush();

//         ClientContext.clear();
//     }

// }
