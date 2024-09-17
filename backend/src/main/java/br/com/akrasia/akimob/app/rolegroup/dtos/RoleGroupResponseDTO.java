package br.com.akrasia.akimob.app.rolegroup.dtos;

import br.com.akrasia.akimob.app.rolegroup.RoleGroup;
import lombok.Getter;

@Getter
public class RoleGroupResponseDTO {

    private Long id;

    public RoleGroupResponseDTO(RoleGroup roleGroup) {
        this.id = roleGroup.getId();
    }

}
