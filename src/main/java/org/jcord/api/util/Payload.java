package org.jcord.api.util;

public final class Payload {
    private static String string;
    private static boolean shouldClose;

    private Payload() {}

    public static void send(String string) {
        Payload.string = string;
    }
    public static String flush() {
        String temp = string;
        string = null;
        return temp;
    }
    public static boolean empty() {
        return string == null;
    }
    public static boolean shouldClose() {
        return shouldClose;
    }
    public static void close() {
        shouldClose = true;
    }
}
