package br.com.akrasia.akimob.core.client.context;

import java.util.Objects;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CurrentClientIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    @Override
    public String resolveCurrentTenantIdentifier() {
        return Objects.requireNonNullElse(ClientContext.getCurrentClient(), "public");
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
