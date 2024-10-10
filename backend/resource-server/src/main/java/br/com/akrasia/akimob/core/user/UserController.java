package br.com.akrasia.akimob.core.user;

import org.springframework.web.bind.annotation.RestController;

import br.com.akrasia.akimob.core.user.dtos.UserInfoDTO;

import java.util.Collection;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getUserInfo(JwtAuthenticationToken principal) {
        Jwt jwt = principal.getToken();

        Boolean superadmin = jwt.getClaimAsBoolean("superadmin");
        Map<String, Collection<String>> clientsUsers = jwt.getClaim("authorities");

        UserInfoDTO userInfo = new UserInfoDTO(superadmin, clientsUsers);
        return ResponseEntity.ok(userInfo);
    }

}
