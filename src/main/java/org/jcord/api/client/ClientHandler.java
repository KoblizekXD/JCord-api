package org.jcord.api.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.jcord.api.util.ResponseHandler;

import java.nio.charset.StandardCharsets;

public class ClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final ResponseHandler handler;

    public ClientHandler(ResponseHandler handler) {
        this.handler = handler;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        int code;
        String content = "";
        if (msg instanceof HttpResponse response) {
            code = response.status().code();
        } else {
            code = -1;
        }
        if (msg instanceof HttpContent httpContent) {
            content = httpContent.content().toString(StandardCharsets.UTF_8);

            if (httpContent instanceof LastHttpContent) {
                ctx.close();
            }
        }
        handler.receive(content);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        DiscordClient.LOGGER.error("Caught exception:", cause);
        ctx.close();
    }
}
