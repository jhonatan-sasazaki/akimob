package br.com.akrasia.akimob.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.superadmin.Superadmin;
import br.com.akrasia.akimob.user.dtos.UserCreateDTO;
import br.com.akrasia.akimob.user.dtos.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        log.info("Creating user: {}", userCreateDTO.username());

        User user = new User();
        user.setUsername(userCreateDTO.username());
        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));
        user.setEmail(userCreateDTO.email());
        user = userRepository.save(user);

        log.info("User {} created: {}", user.getUsername(), user.getId());
        return new UserResponseDTO(user);
    }

    public UserResponseDTO createSuperadmin(UserCreateDTO userCreateDTO) {
        log.info("Creating superadmin: {}", userCreateDTO.username());

        User user = new User();
        user.setUsername(userCreateDTO.username());
        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));
        user.setEmail(userCreateDTO.email());
        user.setSuperadmin(new Superadmin(user));
        user = userRepository.save(user);

        log.info("Superadmin {} created: {}", user.getUsername(), user.getId());
        return new UserResponseDTO(user);
    }

    public PagedModel<UserResponseDTO> listUsers(Pageable pageable) {
        log.debug("Listing users");

        Page<User> users = userRepository.findAll(pageable);
        return new PagedModel<>(users.map(UserResponseDTO::new));
    }

}
