package br.com.akrasia.akimob.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.client.dtos.ClientCreateDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client createClient(ClientCreateDTO clientCreateDTO) {
        Client client = new Client();
        String clientName = StringUtils.normalizeSpace(clientCreateDTO.getName());
        client.setName(clientName);
        return clientRepository.save(client);
    }
    
}
