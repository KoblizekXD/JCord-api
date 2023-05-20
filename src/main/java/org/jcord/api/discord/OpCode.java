package org.jcord.api.discord;

public enum OpCode {
    DISPATCH(0, "Dispatch", Type.RECEIVE),
    HEARTBEAT(1, "Heartbeat", Type.BOTH),
    IDENTIFY(2, "Identify", Type.SEND),
    PRESENCE_UPDATE(3, "Presence Update", Type.SEND),
    VOICE_STATE_UPDATE(4, "Voice State Update", Type.SEND),
    RESUME(6, "Resume", Type.SEND),
    RECONNECT(7, "Reconnect", Type.RECEIVE),
    REQUEST_GUILD_MEMBERS(8, "Request Guild Members", Type.SEND),
    INVALID_SESSION(9, "Invalid Session", Type.RECEIVE),
    HELLO(10, "Hello", Type.RECEIVE),
    HEARTBEAT_ACK(11, "Heartbeat ACK", Type.RECEIVE);

    private final int code;
    private final String name;
    private final Type type;

    OpCode(int code, String name, Type type) {
        this.code = code;
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    };
    public enum Type {
        SEND,
        RECEIVE,
        BOTH
    }
}
