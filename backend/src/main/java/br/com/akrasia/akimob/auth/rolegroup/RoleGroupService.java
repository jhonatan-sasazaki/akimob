package br.com.akrasia.akimob.auth.rolegroup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.auth.authority.Authority;
import br.com.akrasia.akimob.auth.authority.AuthorityRepository;
import br.com.akrasia.akimob.auth.authority.AuthorityService;
import br.com.akrasia.akimob.auth.rolegroup.dtos.RoleGroupCreateDTO;
import br.com.akrasia.akimob.auth.rolegroup.dtos.RoleGroupResponseDTO;
import br.com.akrasia.akimob.client.Client;
import br.com.akrasia.akimob.multiclient.ClientContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleGroupService {

    private final RoleGroupRepoository roleGroupRepoository;
    private final AuthorityService authorityService;
    private final AuthorityRepository authorityRepository;
    private final SessionFactory sessionFactory;

    public void createDefaultRoleGroups(Client client) {
        log.info("Creating default role groups for client: {}", client.getName());

        HashMap<String, Authority> authorityMap = authorityService.getAuthorityMap();
        Set<RoleGroup> roleGroups = new HashSet<>();

        DefaultRoleGroups.ROLE_GROUPS.forEach((roleGroupName, authorityNames) -> {
            RoleGroup roleGroup = new RoleGroup();

            Set<Authority> authorities = new HashSet<>();
            authorityNames.forEach(authorityName -> {
                authorities.add(authorityMap.get(authorityName));
            });

            roleGroup.setName(roleGroupName);
            roleGroup.setAuthorities(authorities);
            roleGroups.add(roleGroup);
        });

        saveRoleGroup(roleGroups, client);
        log.info("Default role groups created for client: {}", client.getName());

    }

    /*
     * When creating a new client, there is no client context set and even after
     * setting the client context, Hibernate does not resolve it on this scope. So,
     * we use a new session after setting the context.
     */
    private void saveRoleGroup(Set<RoleGroup> roleGroups, Client client) {
        ClientContext.setClientId(client.getId());
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        roleGroups.forEach(session::persist);
        session.getTransaction().commit();
        session.close();
        ClientContext.clear();
    }

    public RoleGroupResponseDTO createRoleGroup(RoleGroupCreateDTO roleGroupCreateDTO) {
        log.info("Creating role group: {}", roleGroupCreateDTO.getName());

        RoleGroup roleGroup = new RoleGroup();
        mapToRoleGroup(roleGroupCreateDTO, roleGroup);

        RoleGroup savedRoleGroup = roleGroupRepoository.save(roleGroup);
        log.info("Role group {} created id: {}", savedRoleGroup.getName(), savedRoleGroup.getId());
        return new RoleGroupResponseDTO(savedRoleGroup);
    }

    public RoleGroupResponseDTO updateRoleGroup(Long roleGroupId, RoleGroupCreateDTO roleGroupCreateDTO) {
        log.info("Updating role group id: {}", roleGroupId);

        RoleGroup roleGroup = roleGroupRepoository.getReferenceById(roleGroupId);
        mapToRoleGroup(roleGroupCreateDTO, roleGroup);

        RoleGroup updatedRoleGroup = roleGroupRepoository.save(roleGroup);
        log.info("Role group id {} updated", updatedRoleGroup.getId());
        return new RoleGroupResponseDTO(updatedRoleGroup);
    }

    private void mapToRoleGroup(RoleGroupCreateDTO roleGroupCreateDTO, RoleGroup roleGroup) {
        roleGroup.setName(roleGroupCreateDTO.getName());
        roleGroup.setDescription(roleGroupCreateDTO.getDescription());

        Set<Authority> authorities = new HashSet<>();
        roleGroupCreateDTO.getAuthorities().forEach(authorityId -> {
            authorities.add(authorityRepository.getReferenceById(authorityId));
        });

        roleGroup.setAuthorities(authorities);
    }

}
