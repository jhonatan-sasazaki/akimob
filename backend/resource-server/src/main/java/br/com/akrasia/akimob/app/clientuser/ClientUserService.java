package br.com.akrasia.akimob.app.clientuser;

import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserDTO;
import br.com.akrasia.akimob.commons.app.rolegroup.RoleGroup;
import br.com.akrasia.akimob.commons.app.rolegroup.RoleGroupRepoository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientUserService {

    private final ClientUserRepository clientUserRepository;
    private final RoleGroupRepoository roleGroupRepoository;

    public void createClientUser(ClientCreateUserDTO clientCreateUserDTO) {
        ClientUser clientUser = new ClientUser();
        RoleGroup roleGroup = roleGroupRepoository.getReferenceById(clientCreateUserDTO.roleGroupId());

        clientUser.setId(clientCreateUserDTO.userId());
        clientUser.setRoleGroup(roleGroup);
        clientUserRepository.save(clientUser);
    }

    public ClientUser getClientUser(Long userId) {
        return clientUserRepository.findById(userId).orElse(null);
    }
}
