package org.jcord.api.websocket.identify;

import com.google.gson.Gson;

import java.io.Serializable;

public final class IdentifyJson {
    public IdentifyJson(String token) {
        d = new Data();
        d.token = token;
        op = 2;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    private int op;
    private Data d;
    private static class Data {
        public Data() {
            properties = new Properties();
            intents = 7;
        }
        private String token;
        private int intents;
        private Properties properties;
        private static class Properties {
            private String os = "linux";
            private String browser = "jcord";
            private String device = "jcord";
        }
    }
}
