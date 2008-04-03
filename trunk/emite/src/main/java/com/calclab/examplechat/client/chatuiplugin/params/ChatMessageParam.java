package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.stanzas.Message;

public class ChatMessageParam {
    private final Chat chat;
    private final Message message;

    public ChatMessageParam(final Chat chat, final Message message) {
        this.chat = chat;
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public Chat getChat() {
        return chat;
    }

}
