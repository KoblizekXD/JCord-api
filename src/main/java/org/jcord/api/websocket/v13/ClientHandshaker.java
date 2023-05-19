package org.jcord.api.websocket.v13;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker13;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;

import static io.netty.handler.codec.http.HttpHeaderNames.ORIGIN;
import static io.netty.handler.codec.http.websocketx.WebSocketVersion.V13;

public class ClientHandshaker extends WebSocketClientHandshaker13 {
    public static ClientHandshaker newHandshaker(URI uri, String protocol, boolean allowExtensions, HttpHeaders headers) {
        return new ClientHandshaker(
                uri, V13, protocol, allowExtensions, headers, 65536,true,
                false,-1
        );
    }
    public static ClientHandshaker newHandshaker(URI uri) {
        return new ClientHandshaker(
                uri, V13, null, true, new DefaultHttpHeaders(), 65536,true,
                false,-1
        );
    }

    public ClientHandshaker(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength) {
        super(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength);
    }

    public ClientHandshaker(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, boolean performMasking, boolean allowMaskMismatch) {
        super(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, performMasking, allowMaskMismatch);
    }

    public ClientHandshaker(URI webSocketURL, WebSocketVersion version, String subprotocol, boolean allowExtensions, HttpHeaders customHeaders, int maxFramePayloadLength, boolean performMasking, boolean allowMaskMismatch, long forceCloseTimeoutMillis) {
        super(webSocketURL, version, subprotocol, allowExtensions, customHeaders, maxFramePayloadLength, performMasking, allowMaskMismatch, forceCloseTimeoutMillis);
    }

    /**
     * Constructs a new Http request, removes origin header
     * @return new Http Request sent over Gateway
     */
    @Override
    protected FullHttpRequest newHandshakeRequest() {
        FullHttpRequest request = super.newHandshakeRequest();
        request.headers().remove(ORIGIN);
        return request;
    }
}
