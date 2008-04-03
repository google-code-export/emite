package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.ChatId;

public class GroupChatSubjectParam {

    public final String subject;
    private final ChatId chatId;

    public GroupChatSubjectParam(final ChatId chatId, final String subject) {
        this.chatId = chatId;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public ChatId getChatId() {
        return chatId;
    }

}
