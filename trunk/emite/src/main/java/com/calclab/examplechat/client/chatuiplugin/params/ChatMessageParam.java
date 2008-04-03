package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.im.chat.ChatDefault;

public class ChatMessageParam {
    private final ChatDefault chatDefault;
    private final String message;

    public ChatMessageParam(final ChatDefault chatDefault, final String message) {
        this.chatDefault = chatDefault;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ChatDefault getChat() {
        return chatDefault;
    }

}
