package com.calclab.examplechat.client.chatuiplugin;


public class GroupChatSubject {

    public final String subject;
    private final String chatId;

    public GroupChatSubject(final String chatId, final String subject) {
        this.chatId = chatId;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public String getChatId() {
        return chatId;
    }

}
