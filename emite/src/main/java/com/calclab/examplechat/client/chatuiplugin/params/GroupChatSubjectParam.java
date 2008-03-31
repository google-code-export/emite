package com.calclab.examplechat.client.chatuiplugin.params;


public class GroupChatSubjectParam {

    public final String subject;
    private final String chatId;

    public GroupChatSubjectParam(final String chatId, final String subject) {
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
