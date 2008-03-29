package com.calclab.examplechat.client.chatuiplugin;

import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;

public class GroupChatSubject {

    public final GroupChat groupChat;
    public final String subject;

    public GroupChatSubject(final GroupChat groupChat, final String subject) {
        this.groupChat = groupChat;
        this.subject = subject;
    }

    public GroupChat getGroupChat() {
        return groupChat;
    }

    public String getSubject() {
        return subject;
    }

}
