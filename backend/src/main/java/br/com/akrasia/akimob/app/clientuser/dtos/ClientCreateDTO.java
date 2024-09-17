package br.com.akrasia.akimob.app.clientuser.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientCreateDTO(
                @NotBlank @Size(min = 1, max = 255) String name,
                @NotBlank @Size(min = 1, max = 63) String schemaName) {

}
