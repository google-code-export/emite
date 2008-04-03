package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.im.chat.Chat;

public class ChatMessageParam {
    private final Chat chat;
    private final String message;

    public ChatMessageParam(final Chat chat, final String message) {
        this.chat = chat;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Chat getChat() {
        return chat;
    }

}
