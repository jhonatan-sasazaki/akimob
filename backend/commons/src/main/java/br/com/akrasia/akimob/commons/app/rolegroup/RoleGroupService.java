package br.com.akrasia.akimob.commons.app.rolegroup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.commons.app.authority.Authority;
import br.com.akrasia.akimob.commons.app.authority.AuthorityRepository;
import br.com.akrasia.akimob.commons.app.authority.AuthorityService;
import br.com.akrasia.akimob.commons.app.rolegroup.dtos.RoleGroupCreateDTO;
import br.com.akrasia.akimob.commons.app.rolegroup.dtos.RoleGroupResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleGroupService {

    private final RoleGroupRepoository roleGroupRepoository;
    private final AuthorityService authorityService;
    private final AuthorityRepository authorityRepository;

    public void createDefaultRoleGroups() {

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

        roleGroupRepoository.saveAll(roleGroups);

    }

    public RoleGroupResponseDTO createRoleGroup(RoleGroupCreateDTO roleGroupCreateDTO) {
        log.info("Creating role group: {}", roleGroupCreateDTO.name());

        RoleGroup roleGroup = new RoleGroup();
        mapToRoleGroup(roleGroupCreateDTO, roleGroup);

        RoleGroup savedRoleGroup = roleGroupRepoository.save(roleGroup);
        log.info("Role group {} created id: {}", savedRoleGroup.getName(), savedRoleGroup.getId());
        return new RoleGroupResponseDTO(savedRoleGroup);
    }

    public RoleGroupResponseDTO updateRoleGroup(Long roleGroupId, RoleGroupCreateDTO roleGroupCreateDTO) {
        log.info("Updating role group id: {}", roleGroupId);

        RoleGroup roleGroup = roleGroupRepoository.findById(roleGroupId).orElseThrow();
        mapToRoleGroup(roleGroupCreateDTO, roleGroup);

        RoleGroup updatedRoleGroup = roleGroupRepoository.save(roleGroup);
        log.info("Role group id {} updated", updatedRoleGroup.getId());
        return new RoleGroupResponseDTO(updatedRoleGroup);
    }

    private void mapToRoleGroup(RoleGroupCreateDTO roleGroupCreateDTO, RoleGroup roleGroup) {
        roleGroup.setName(roleGroupCreateDTO.name());
        roleGroup.setDescription(roleGroupCreateDTO.description());

        Set<Authority> authorities = new HashSet<>();
        roleGroupCreateDTO.authorities().forEach(authorityId -> {
            authorities.add(authorityRepository.getReferenceById(authorityId));
        });

        roleGroup.setAuthorities(authorities);
    }

}
