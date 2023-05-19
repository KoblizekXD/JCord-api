package org.jcord.api.websocket.v13;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import org.jcord.api.websocket.Gateway;

public class SocketHandler extends SimpleChannelInboundHandler<Object> {
    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public ChannelPromise handshakeFuture() {
        return handshakeFuture;
    }

    public SocketHandler(ClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Gateway.LOGGER.warn("Channel closure detected");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(channel, (FullHttpResponse) msg);
                handshakeFuture.setSuccess();
            } catch (WebSocketClientHandshakeException e) {
                Gateway.LOGGER.error("Handshake was not successful, see stack trace for further details: ", e);
                handshakeFuture.setFailure(e);
            }
            return;
        }
        // TODO: Finish data obtain
    }
}
