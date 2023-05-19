package org.jcord.api.websocket;

import org.jcord.api.discord.API;

public final class Gateway {
    private final String token;
    private final API api;

    private Gateway(String token, API api) {
        this.token = token;
        this.api = api;
    }
    public static Gateway of(String token) {
        return new Gateway(token, API.V6);
    }
}
