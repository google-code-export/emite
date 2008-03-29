package com.calclab.examplechat.client.chatuiplugin.utils;

import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.examplechat.client.chatuiplugin.AbstractChatInputMessage;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogPlugin;
import com.calclab.examplechat.client.chatuiplugin.CreateGroupChatActionParams;
import com.calclab.examplechat.client.chatuiplugin.GroupChatSubject;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class MultiChatSamples {
    public static void show(final DefaultDispatcher dispatcher, final String currentUserJid) {
        Log.debug("Adding pair chats");
        String chatId1 = "ma@example.com";
        PairChatUser pairUser = new PairChatUser(chatId1, "Mark");
        dispatcher.fire(ChatDialogPlugin.CREATE_PAIR_CHAT, pairUser);
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "hello"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid, "hi"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, chatId1, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId1, currentUserJid,
                "testing"));

        String chatId2 = "fran@example.com";
        PairChatUser pairUser2 = new PairChatUser(chatId2, "frank");
        dispatcher.fire(ChatDialogPlugin.CREATE_PAIR_CHAT, pairUser2);
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "hello"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid, "hi"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, chatId2, "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new AbstractChatInputMessage(chatId2, currentUserJid,
                "testing"));

        Log.debug("Adding group chats");
        String chatRoomId1 = "chat1@rooms.localhost";
        dispatcher.fire(ChatDialogPlugin.CREATE_GROUP_CHAT, new CreateGroupChatActionParams(chatRoomId1, "luther.b",
                GroupChatUser.MODERADOR));
        String chatRoomId2 = "chat2@rooms.localhost";
        dispatcher.fire(ChatDialogPlugin.CREATE_GROUP_CHAT, new CreateGroupChatActionParams(chatRoomId2, "luther",
                GroupChatUser.PARTICIPANT));

        dispatcher.fire(ChatDialogPlugin.SET_GROUPCHAT_SUBJECT, new GroupChatSubject(chatRoomId1,
                "Welcome to chat1, today topic: Cultural issues in Brazil"));
        // groupChat2.setSubject("Welcome to this room: we are talking today
        // about
        // 2009 meeting");

        // Log.debug("Adding group users");
        // GroupChatUser lutherb = new GroupChatUser("groucho@localhost",
        // "luther.b", "blue", GroupChatUser.MODERADOR);

        // GroupChatUser luther = new GroupChatUser("luther@localhost",
        // "luther",
        // "blue", GroupChatUser.MODERADOR);
        // GroupChatUser otherUser = new GroupChatUser("otheruser", "ouser",
        // "red",
        // GroupChatUser.MODERADOR);
        // GroupChatUser otherUser2 = new GroupChatUser("luther", "luther",
        // "green",
        // GroupChatUser.MODERADOR);
        //
        // groupChat1.addUser(lutherb);
        // groupChat1.addUser(otherUser);
        // groupChat2.addUser(luther);
        // groupChat2.addUser(otherUser2);
        //
        // Log.debug("Adding subjects");

        //
        // Log.debug("Adding messages");
        // groupChat1.addMessage("luther.b", "Test message in group chat 1");
        // groupChat2.addMessage("luther", "Mensaje de test en group chat 2");
        //
        // Log.debug("Adding other messages");
        // groupChat2.addInfoMessage("Mensaje de evento en group chat 2");
        // groupChat2.addDelimiter("17:35");
        //
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        //
    }
}
