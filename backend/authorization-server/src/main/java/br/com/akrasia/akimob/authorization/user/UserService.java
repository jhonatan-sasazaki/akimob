package br.com.akrasia.akimob.authorization.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.akrasia.akimob.authorization.OAuthUser;
import br.com.akrasia.akimob.commons.app.clientuser.ClientUser;
import br.com.akrasia.akimob.commons.app.clientuser.ClientUserRepository;
import br.com.akrasia.akimob.commons.core.client.context.ClientContext;
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

        Map<String, ClientUser> clientsUsers = fetchUserClients(user);
        OAuthUser oauthUser = new OAuthUser(user, clientsUsers);

        return oauthUser;
    }

    private Map<String, ClientUser> fetchUserClients(User user) {
        Map<String, ClientUser> clientsUsers = new HashMap<>();
        user.getClients().forEach(client -> {
            ClientContext.setCurrentClient(client.getSchemaName());

            Optional<ClientUser> clientUser = clientUserRepository.findById(user.getId());
            clientUser.ifPresent(clientUserFound -> clientsUsers.put(client.getSchemaName(), clientUserFound));

            ClientContext.clear();
        });
        return clientsUsers;
    }

}
