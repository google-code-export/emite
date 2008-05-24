package com.calclab.emiteuiplugin.client.chat;

public class ChatUIStartedByMe {
    private final boolean startedByMe;

    public ChatUIStartedByMe(final boolean startedByMe) {
        this.startedByMe = startedByMe;
    }

    public boolean isStartedByMe() {
        return startedByMe;
    }
}
