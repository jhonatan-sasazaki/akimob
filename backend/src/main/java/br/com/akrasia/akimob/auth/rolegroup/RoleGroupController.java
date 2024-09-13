package br.com.akrasia.akimob.auth.rolegroup;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.auth.rolegroup.dtos.RoleGroupCreateDTO;
import br.com.akrasia.akimob.auth.rolegroup.dtos.RoleGroupResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rolegroups")
@RequiredArgsConstructor
public class RoleGroupController {

    private final RoleGroupService roleGroupService;

    @PostMapping
    @PreAuthorize("hasAuthority('role_group_add')")
    public ResponseEntity<RoleGroupResponseDTO> createRoleGroup(@RequestBody @Valid RoleGroupCreateDTO roleGroupCreateDTO) {
        RoleGroupResponseDTO roleGroup = roleGroupService.createRoleGroup(roleGroupCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleGroup);
    }
    
}
