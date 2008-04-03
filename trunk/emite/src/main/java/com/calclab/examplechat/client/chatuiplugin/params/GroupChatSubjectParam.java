package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.im.chat.Chat;

public class GroupChatSubjectParam {

    public final String subject;
    private final Chat chat;

    public GroupChatSubjectParam(final Chat chat, final String subject) {
        this.chat = chat;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public Chat getChat() {
        return chat;
    }

}
