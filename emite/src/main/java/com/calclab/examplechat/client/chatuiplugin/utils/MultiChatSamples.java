package com.calclab.examplechat.client.chatuiplugin.utils;

import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class MultiChatSamples {
    public static void show(final MultiChat extChatDialog) {
        PairChatUser pairUser = new PairChatUser("Mark", "ma");
        PairChat pairChat = extChatDialog.createPairChat(pairUser);
        pairChat.addMessage("ma", "testing");

        GroupChat groupChat1 = extChatDialog.createGroupChat("chat1@rooms.localhost", "luther.b",
                GroupChatUser.PARTICIPANT);
        GroupChat groupChat2 = extChatDialog.createGroupChat("chat2@rooms.localhost", "luther",
                GroupChatUser.PARTICIPANT);

        GroupChatUser otherUser = new GroupChatUser("other user", "otheruser", "red", GroupChatUser.MODERADOR);
        GroupChatUser otherUser2 = new GroupChatUser("luther", "luther", "green", GroupChatUser.MODERADOR);

        groupChat1.setSubject("Welcome to chat1, today topic: Cultural issues in Brazil");
        groupChat1.addUser(otherUser);
        groupChat1.addMessage("luther.b", "Test message in group chat 1");

        groupChat2.setSubject("Welcome to this room: we are talking today about 2009 meeting");
        groupChat2.addUser(otherUser2);
        groupChat2.addMessage("luther", "Mensaje de test en group chat 2");
        groupChat2.addInfoMessage("Mensaje de evento en group chat 2");
        groupChat2.addDelimiter("17:35");

    }
}
