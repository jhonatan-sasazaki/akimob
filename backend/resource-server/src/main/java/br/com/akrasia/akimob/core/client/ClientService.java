package br.com.akrasia.akimob.core.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.app.clientuser.ClientUserService;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserDTO;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserResponseDTO;
import br.com.akrasia.akimob.commons.app.rolegroup.RoleGroupService;
import br.com.akrasia.akimob.commons.core.client.Client;
import br.com.akrasia.akimob.commons.core.client.context.ClientContext;
import br.com.akrasia.akimob.commons.core.user.User;
import br.com.akrasia.akimob.core.client.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.core.client.dtos.ClientResponseDTO;
import br.com.akrasia.akimob.core.user.UserRepository;
import br.com.akrasia.akimob.database.flyway.FlywayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final RoleGroupService roleGroupService;
    private final UserRepository userRepository;
    private final ClientUserService clientUserService;
    private final FlywayService flywayService;

    public ClientResponseDTO createClient(ClientCreateDTO clientCreateDTO) {
        log.info("Creating client: {}", clientCreateDTO.name());

        Client client = new Client();
        String clientName = StringUtils.normalizeSpace(clientCreateDTO.name());
        client.setName(clientName);
        client.setSchemaName(clientCreateDTO.schemaName());
        flywayService.initNewClientSchema(clientCreateDTO.schemaName());

        Client savedClient = clientRepository.save(client);
        createClientDefaultRoleGroups(savedClient.getSchemaName());

        log.info("Client created: {}", clientName);
        return new ClientResponseDTO(savedClient);
    }

    public PagedModel<ClientResponseDTO> listClients(Pageable pageable) {
        log.info("Listing clients");

        Page<Client> clientsPage = clientRepository.findAll(pageable);
        return new PagedModel<>(clientsPage.map(ClientResponseDTO::new));
    }

    private void createClientDefaultRoleGroups(String schemaName) {
        log.info("Creating default role groups for schema {}", schemaName);

        ClientContext.setCurrentClient(schemaName);
        roleGroupService.createDefaultRoleGroups();
        ClientContext.clear();

        log.info("Default role groups created for schema {}", schemaName);
    }

    public ClientCreateUserResponseDTO createClientUser(Long clientId, ClientCreateUserDTO clientCreateUserDTO) {
        log.info("Creating user {} for client {}", clientCreateUserDTO, clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + clientId));

        User user = userRepository.findById(clientCreateUserDTO.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + clientCreateUserDTO.userId()));

        ClientContext.setCurrentClient(client.getSchemaName());
        clientUserService.createClientUser(clientCreateUserDTO);
        ClientContext.clear();

        user.getClients().add(client);
        userRepository.save(user);

        log.info("User {} created for client {}", clientCreateUserDTO.userId(), clientId);
        return new ClientCreateUserResponseDTO(clientId, clientCreateUserDTO.userId());
    }

}
