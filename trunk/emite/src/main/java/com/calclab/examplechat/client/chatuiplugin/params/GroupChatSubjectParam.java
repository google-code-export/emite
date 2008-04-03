package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.im.chat.ChatDefault;

public class GroupChatSubjectParam {

    public final String subject;
    private final ChatDefault chatDefault;

    public GroupChatSubjectParam(final ChatDefault chatDefault, final String subject) {
        this.chatDefault = chatDefault;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public ChatDefault getChat() {
        return chatDefault;
    }

}
