package br.com.akrasia.akimob.client;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.client.dtos.ClientCreateDTO;
import br.com.akrasia.akimob.client.dtos.ClientResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientResponseDTO createClient(ClientCreateDTO clientCreateDTO) {
        log.info("Creating client: {}", clientCreateDTO.name());

        Client client = new Client();
        String clientName = StringUtils.normalizeSpace(clientCreateDTO.name());
        client.setName(clientName);
        clientRepository.save(client);

        log.info("Client created: {}", clientName);
        return new ClientResponseDTO(client);
    }

    public List<ClientResponseDTO> listClients(Pageable pageable) {
        log.info("Listing clients");

        Page<Client> clientsPage = clientRepository.findAll(pageable);
        return clientsPage.stream()
                .map(ClientResponseDTO::new)
                .collect(Collectors.toList());
    }

}
