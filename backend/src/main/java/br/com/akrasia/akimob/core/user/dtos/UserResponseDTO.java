package br.com.akrasia.akimob.core.user.dtos;

import br.com.akrasia.akimob.core.user.User;

public record UserResponseDTO(Long id, String username, String email) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getUsername(), user.getEmail());
    }

}
