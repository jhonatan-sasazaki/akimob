package br.com.akrasia.akimob.authorization.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.authorization.OAuthUser;
import br.com.akrasia.akimob.commons.app.clientuser.ClientUser;
import br.com.akrasia.akimob.commons.app.clientuser.ClientUserRepository;
import br.com.akrasia.akimob.commons.core.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ClientUserRepository clientUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ClientUser clientUser = null;
        try {
            clientUser = clientUserRepository.findById(user.getId()).orElse(null);
        } catch (Exception e) {
            log.warn("ClientUser not found for user: {}, message: {}", user.getUsername(), e.getMessage());
        }

        OAuthUser oauthUser = new OAuthUser(user, clientUser);

        return oauthUser;
    }

}
