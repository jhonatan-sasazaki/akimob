package br.com.akrasia.akimob.commons.app.rolegroup.dtos;

import br.com.akrasia.akimob.commons.app.rolegroup.RoleGroup;

public record RoleGroupResponseDTO(Long id) {

    public RoleGroupResponseDTO(RoleGroup roleGroup) {
        this(roleGroup.getId());
    }

}
