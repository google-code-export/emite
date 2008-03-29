package com.calclab.examplechat.client.chatuiplugin;

public class AbstractChatOutputMessage {

    private final AbstractChat chat;
    private final String message;

    public AbstractChatOutputMessage(final AbstractChat chat, final String message) {
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
