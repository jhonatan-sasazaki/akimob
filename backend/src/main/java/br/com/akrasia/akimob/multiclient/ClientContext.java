package br.com.akrasia.akimob.multiclient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ClientContext {

    private static final ThreadLocal<Long> currentClient = new InheritableThreadLocal<>();

    public static void setClientId(Long clientId) {
        log.debug("Setting clientId to {}", clientId);
        currentClient.set(clientId);
    }

    public static Long getClientId() {
        return currentClient.get();
    }

    public static void clear() {
        currentClient.remove();
    }

}
