package br.com.akrasia.akimob.app.rolegroup.dtos;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoleGroupCreateDTO {

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;
    private String description;
    private Set<Long> authorities;

}
