package org.jcord.api.util;

public interface IGateway {
    void run();
    default void runAsync() {
        new Thread(this::run).start();
    }
}
