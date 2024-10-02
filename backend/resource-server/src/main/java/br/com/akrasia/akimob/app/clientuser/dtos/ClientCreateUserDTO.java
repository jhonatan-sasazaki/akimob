package br.com.akrasia.akimob.app.clientuser.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ClientCreateUserDTO(@NotNull @Min(1) Long userId, @NotNull @Min(1) Long roleGroupId) {

}
