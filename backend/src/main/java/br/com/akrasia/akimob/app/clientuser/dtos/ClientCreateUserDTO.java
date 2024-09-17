package br.com.akrasia.akimob.app.clientuser.dtos;

import jakarta.validation.constraints.Min;

public record ClientCreateUserDTO(@Min(1) Long userId, @Min(1) Long roleGroupId) {

}
