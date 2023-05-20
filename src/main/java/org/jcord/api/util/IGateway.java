package org.jcord.api.util;

public interface IGateway {
    void run(ResponseHandler response);
    default void runAsync(ResponseHandler response) {
        new Thread(() -> run(response)).start();
    }
}
