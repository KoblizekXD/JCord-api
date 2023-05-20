package org.jcord.api.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jcord.api.util.ResponseHandler;
import org.jcord.api.util.SSL;

import java.util.ArrayList;

public class DiscordClient {
    public static final Logger LOGGER = LogManager.getLogger();
    private final String token;

    public DiscordClient(String token) {
        this.token = token;
    }
    public void get(String endpoint, boolean useToken, ResponseHandler handler) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
        bootstrap.handler(new ClientInitializer(new SSL().getSSL(), handler));
        try {
            Channel channel = bootstrap.connect("discord.com", 443).sync().channel();
            HttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1,
                    HttpMethod.GET,
                    "https://www.discord.com" + endpoint,
                    Unpooled.EMPTY_BUFFER
            );
            request.headers().set(HttpHeaderNames.HOST, "discord.com");
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED);
            if (useToken) request.headers().set("Authorization", "Bot " + token);

            channel.writeAndFlush(request);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("Synchronization has been interrupted, disconnecting...");
            Thread.currentThread().interrupt();
        } finally {
            group.shutdownGracefully();
        }
    }
}
