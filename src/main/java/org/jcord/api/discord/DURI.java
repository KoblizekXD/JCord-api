package org.jcord.api.discord;

import java.net.URI;

public final class DURI {
    private DURI() {}

    public static URI getURI(API api) {
        return URI.create("wss://gateway.discord.gg/?v=" + api.asInteger() + "&encoding=json");
    }
}
