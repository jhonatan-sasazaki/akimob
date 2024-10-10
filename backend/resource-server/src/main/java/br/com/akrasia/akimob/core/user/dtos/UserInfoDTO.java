package br.com.akrasia.akimob.core.user.dtos;

import java.util.Collection;
import java.util.Map;

public record UserInfoDTO(Boolean superadmin, Map<String, Collection<String>> clients) {
}
