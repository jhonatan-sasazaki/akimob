package br.com.akrasia.akimob.core.client.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ClientResolver {

    @Value("${akimob.multiclient.http.header-name}")
    private String clientIdHeaderName;

    public String resolveClient(HttpServletRequest request) {
        return request.getHeader(clientIdHeaderName);
    }

}
