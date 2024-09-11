package br.com.akrasia.akimob.multiclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ClientResolver {

    @Value("${akimob.multiclient.http.header-name}")
    private String clientIdHeaderName;

    public Long resolveClientId(HttpServletRequest request) {
        String clientIdHeader = request.getHeader(clientIdHeaderName);

        if (clientIdHeader == null) {
            log.debug("clientId header not found");
            return null;
        }

        try {
            return Long.parseLong(request.getHeader(clientIdHeaderName));
        } catch (NumberFormatException e) {
            log.error("Error parsing clientId header: {}", clientIdHeader);
            return null;
        }
    }

}
