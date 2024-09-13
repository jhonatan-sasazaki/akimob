package br.com.akrasia.akimob.client.dtos;

import jakarta.validation.constraints.Min;

public record ClientCreateUserDTO(@Min(1) Long userId, @Min(1) Long roleGroupId) {

}
