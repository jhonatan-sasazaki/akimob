package br.com.akrasia.akimob.commons.app.rolegroup.dtos;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleGroupCreateDTO(
        @NotBlank @Size(min = 1, max = 255) String name,
        String description,
        Set<Long> authorities) {
}
