package br.com.akrasia.akimob.app.clientuser.dtos;

import java.util.Set;

public record ClientUserDTO(String username, Set<String> authorities) {

}
