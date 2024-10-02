package br.com.akrasia.akimob.app.clientuser;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.app.clientuser.dtos.ClientCreateUserDTO;
import br.com.akrasia.akimob.app.clientuser.dtos.ClientUserDTO;
import br.com.akrasia.akimob.app.rolegroup.RoleGroup;
import br.com.akrasia.akimob.app.rolegroup.RoleGroupRepoository;
import br.com.akrasia.akimob.core.authentication.ClientUserAuthenticationToken;
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

    public ClientUserDTO getClientUserDetails(ClientUserAuthenticationToken authentication) {
        String username = authentication.getName();
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(HashSet::new, Set::add, Set::addAll);
        return new ClientUserDTO(username, authorities);
    }

}
