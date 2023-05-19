package org.jcord.api.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.logging.log4j.LogManager;

import javax.net.ssl.SSLException;

public final class SSL {
    private SslContext ctx;

    public SSL() {
        try {
            ctx = SslContextBuilder.forClient()
                   .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } catch (SSLException e) {
            LogManager.getLogger().error("Connection secure failed");
            ctx = null;
        }
    }

    public SslContext getSSL() {
        return ctx;
    }
}
