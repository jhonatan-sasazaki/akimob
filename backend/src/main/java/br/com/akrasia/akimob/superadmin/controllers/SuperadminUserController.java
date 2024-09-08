package br.com.akrasia.akimob.superadmin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.user.UserService;
import br.com.akrasia.akimob.user.dtos.UserCreateDTO;
import br.com.akrasia.akimob.user.dtos.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/superadmin/users")
@RequiredArgsConstructor
public class SuperadminUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        UserResponseDTO user = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/superadmin")
    public ResponseEntity<UserResponseDTO> createSuperadmin(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        UserResponseDTO user = userService.createSuperadmin(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
