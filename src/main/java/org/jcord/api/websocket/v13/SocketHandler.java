package org.jcord.api.websocket.v13;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Gateway.LOGGER.error("An unknown exception was thrown: ", cause);
        ctx.close();
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
        if (msg instanceof FullHttpResponse response) {
            Gateway.LOGGER.error("Unexpected fatal error: ", new IllegalStateException(
                    "Unexpected Http Response (status = " + response.status() +
                            ", content = " + response.content().toString(CharsetUtil.UTF_8) + ')'));
        }
        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame textFrame) {
            String text = textFrame.text();
        } else if (frame instanceof CloseWebSocketFrame) {
            Gateway.LOGGER.warn("WebSocket requested closure, disconnecting...");
            channel.close();
        }
    }
}
