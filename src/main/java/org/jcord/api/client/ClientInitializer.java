package org.jcord.api.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.ssl.SslContext;
import org.jcord.api.util.ResponseHandler;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext context;
    private final ResponseHandler handler;

    public ClientInitializer(SslContext context, ResponseHandler handler) {
        this.context = context;
        this.handler = handler;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (context != null) {
            p.addLast(context.newHandler(ch.alloc()));
        }

        p.addLast(new HttpClientCodec());
        p.addLast(new HttpContentDecompressor());
        p.addLast(new ClientHandler(handler));
    }
}
