package br.com.akrasia.akimob.auth.rolegroup.dtos;

import br.com.akrasia.akimob.auth.rolegroup.RoleGroup;
import lombok.Getter;

@Getter
public class RoleGroupResponseDTO {

    private Long id;

    public RoleGroupResponseDTO(RoleGroup roleGroup) {
        this.id = roleGroup.getId();
    }

}
