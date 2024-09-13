package br.com.akrasia.akimob.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.auth.rolegroup.RoleGroup;
import br.com.akrasia.akimob.auth.rolegroup.RoleGroupRepoository;
import br.com.akrasia.akimob.auth.rolegroup.RoleGroupService;
import br.com.akrasia.akimob.client.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.client.dtos.ClientCreateUserDTO;
import br.com.akrasia.akimob.client.dtos.ClientCreateUserResponseDTO;
import br.com.akrasia.akimob.client.dtos.ClientResponseDTO;
import br.com.akrasia.akimob.client.repositories.ClientRepository;
import br.com.akrasia.akimob.client.repositories.ClientUserRepository;
import br.com.akrasia.akimob.user.User;
import br.com.akrasia.akimob.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientUserRepository clientUserRepository;
    private final RoleGroupService roleGroupService;
    private final UserRepository userRepository;
    private final RoleGroupRepoository roleGroupRepoository;

    public ClientResponseDTO createClient(ClientCreateDTO clientCreateDTO) {
        log.info("Creating client: {}", clientCreateDTO.name());

        Client client = new Client();
        String clientName = StringUtils.normalizeSpace(clientCreateDTO.name());
        client.setName(clientName);

        Client savedClient = clientRepository.save(client);

        roleGroupService.createDefaultRoleGroups(savedClient);

        log.info("Client created: {}", clientName);
        return new ClientResponseDTO(savedClient);
    }

    public PagedModel<ClientResponseDTO> listClients(Pageable pageable) {
        log.info("Listing clients");

        Page<Client> clientsPage = clientRepository.findAll(pageable);
        return new PagedModel<>(clientsPage.map(ClientResponseDTO::new));
    }

    public ClientCreateUserResponseDTO createClientUser(Long clientId, @Min(1) @Valid ClientCreateUserDTO clientCreateUserDTO) {
        log.info("Creating user {} for client {}", clientCreateUserDTO, clientId);

        Client client = clientRepository.getReferenceById(clientId);
        User user = userRepository.getReferenceById(clientCreateUserDTO.userId());
        RoleGroup roleGroup = roleGroupRepoository.getReferenceById(clientCreateUserDTO.roleGroupId());

        ClientUser clientUser = new ClientUser();
        clientUser.setClient(client);
        clientUser.setUser(user);
        clientUser.setRoleGroup(roleGroup);

        clientUserRepository.save(clientUser);

        log.info("User {} created for client {}", clientCreateUserDTO.userId(), clientId);
        return new ClientCreateUserResponseDTO(clientCreateUserDTO.userId(), clientId);
    }

}
