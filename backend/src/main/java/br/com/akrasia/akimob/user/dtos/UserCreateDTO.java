package br.com.akrasia.akimob.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank @Size(min = 1, max = 255) String username,
        @NotBlank @Size(min = 8, max = 64) String password,
        @NotBlank @Email(regexp = ".*@.+") String email) {

}
