package br.com.akrasia.akimob.commons.app.authority;

import java.util.HashMap;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public HashMap<String, Authority> getAuthorityMap() {
        HashMap<String, Authority> authorityMap = new HashMap<>();
        authorityRepository.findAll().forEach(authority -> {
            authorityMap.put(authority.getName(), authority);
        });
        return authorityMap;
    }

}
