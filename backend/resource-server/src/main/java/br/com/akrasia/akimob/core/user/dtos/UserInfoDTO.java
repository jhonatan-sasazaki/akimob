package br.com.akrasia.akimob.core.user.dtos;

import java.util.Collection;
import java.util.Map;

public record UserInfoDTO(String subject, Boolean superadmin, Map<String, Collection<String>> clients) {
}
