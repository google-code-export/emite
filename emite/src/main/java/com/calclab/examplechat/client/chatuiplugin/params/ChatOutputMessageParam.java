package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;

public class ChatOutputMessageParam {

    private final AbstractChat chat;
    private final String message;

    public ChatOutputMessageParam(final AbstractChat chat, final String message) {
        this.chat = chat;
        this.message = message;
    }

    public AbstractChat getChat() {
        return chat;
    }

    public String getMessage() {
        return message;
    }

}
