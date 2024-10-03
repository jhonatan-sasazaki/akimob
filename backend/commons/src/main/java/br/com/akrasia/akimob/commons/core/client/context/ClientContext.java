package br.com.akrasia.akimob.commons.core.client.context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ClientContext {

    private static final ThreadLocal<String> currentClient = new InheritableThreadLocal<>();

    public static void setCurrentClient(String client) {
        log.debug("Setting current client to {}", client);
        currentClient.set(client);
    }

    public static String getCurrentClient() {
        return currentClient.get();
    }

    public static void clear() {
        currentClient.remove();
    }

}
