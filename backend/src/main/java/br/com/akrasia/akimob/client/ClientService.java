package br.com.akrasia.akimob.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.auth.rolegroup.RoleGroupService;
import br.com.akrasia.akimob.client.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.client.dtos.ClientCreateUserResponseDTO;
import br.com.akrasia.akimob.client.dtos.ClientResponseDTO;
import br.com.akrasia.akimob.user.User;
import br.com.akrasia.akimob.user.UserRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final RoleGroupService roleGroupService;
    private final UserRepository userRepository;

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

    public ClientCreateUserResponseDTO createClientUser(Long clientId, @Min(1) Long userId) {
        log.info("Creating user {} for client {}", userId, clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        client.getUsers().add(user);
        clientRepository.save(client);

        log.info("User {} created for client {}", userId, clientId);
        return new ClientCreateUserResponseDTO(userId, clientId);
    }

}
