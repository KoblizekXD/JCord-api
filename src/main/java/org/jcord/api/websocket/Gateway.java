package org.jcord.api.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jcord.api.discord.API;
import org.jcord.api.discord.DURI;
import org.jcord.api.util.IGateway;
import org.jcord.api.util.Payload;
import org.jcord.api.util.ResponseHandler;
import org.jcord.api.util.SSL;
import org.jcord.api.websocket.v13.ClientHandshaker;
import org.jcord.api.websocket.v13.SocketHandler;

import java.net.URI;

// TODO: heartbeating, identify with token
public final class Gateway implements IGateway {
    public static final Logger LOGGER = LogManager.getLogger("JCord/Gateway");
    private final String token;
    private final API api;

    private Gateway(String token, API api) {
        this.token = token;
        this.api = api;
    }
    public static Gateway of(String token) {
        return new Gateway(token, API.V6);
    }
    public static Gateway of(String token, API api) {
        return new Gateway(token, api);
    }

    @Override
    public void run(ResponseHandler response) {
        URI uri = DURI.getURI(api);
        final SslContext ssl = new SSL().getSSL();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            final SocketHandler handler = new SocketHandler(ClientHandshaker.newHandshaker(uri), response);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            ChannelPipeline p = channel.pipeline();
                            if (ssl != null) {
                                p.addLast(ssl.newHandler(channel.alloc(), "gateway.discord.gg", 443));
                            } else {
                                LOGGER.warn("Valid SSL not detected, is it enabled?");
                            }
                            p.addLast(new HttpClientCodec(),
                                    new HttpObjectAggregator(8192),
                                    WebSocketClientCompressionHandler.INSTANCE, handler);
                        }
                    });
            Channel channel = bootstrap.connect(uri.getHost(), 443).sync().channel();
            handler.handshakeFuture().sync();

            while (true) {
                if (!Payload.empty()) {
                    WebSocketFrame frame = new TextWebSocketFrame(Payload.flush());
                    channel.writeAndFlush(frame);
                } else if (Payload.shouldClose()) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error("Thread was interrupted during sync?", e);
            Thread.currentThread().interrupt();
        } finally {
            group.shutdownGracefully();
        }
    }
}
