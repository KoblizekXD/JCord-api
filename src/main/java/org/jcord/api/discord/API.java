package org.jcord.api.discord;

public enum API {
    V10,
    V9,
    V8(Status.DEPRECATED),
    V7(Status.DEPRECATED),
    V6(Status.DEPRECATED, true),
    V5(Status.DISCONTINUED),
    V4(Status.DISCONTINUED),
    V3(Status.DISCONTINUED);

    private final Status status;
    private final boolean isDefault;

    API(Status status, boolean isDefault) {
        this.status = status;
        this.isDefault = isDefault;
    }

    API() {
        this(Status.AVAILABLE, false);
    }
    API(Status status) {
        this(status, false);
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Status getStatus() {
        return status;
    }
    public int asInteger() {
        return Integer.parseInt(this.toString().substring(1));
    }
    public enum Status {
        AVAILABLE,
        DEPRECATED,
        DISCONTINUED
    }
}
