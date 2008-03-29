package com.calclab.examplechat.client.chatuiplugin.utils;

import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogPlugin;
import com.calclab.examplechat.client.chatuiplugin.CreateGroupChatActionParams;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class MultiChatSamples {
    public static void show(final DefaultDispatcher dispatcher) {
        Log.debug("Adding pair chats");
        PairChatUser pairUser = new PairChatUser("Mark", "ma");
        dispatcher.fire(ChatDialogPlugin.CREATE_PAIR_CHAT, pairUser);
        // PairChat pairChat = extChatDialog.createPairChat(pairUser);
        // pairChat.addMessage("ma", "foo");
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
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        // pairChat.addMessage("ma", "testing");
        //
        PairChatUser pairUser2 = new PairChatUser("Fran", "frank");
        dispatcher.fire(ChatDialogPlugin.CREATE_PAIR_CHAT, pairUser2);
        // PairChat pairChat2 = extChatDialog.createPairChat(pairUser2);
        // pairChat2.addMessage("frank", "foo");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        // pairChat2.addMessage("frank", "testing");
        //
        Log.debug("Adding group chats");
        dispatcher.fire(ChatDialogPlugin.CREATE_GROUP_CHAT, new CreateGroupChatActionParams("chat1@rooms.localhost",
                "luther.b", GroupChatUser.PARTICIPANT));
        dispatcher.fire(ChatDialogPlugin.CREATE_GROUP_CHAT, new CreateGroupChatActionParams("chat2@rooms.localhost",
                "luther", GroupChatUser.PARTICIPANT));
        //
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

        // groupChat1.setSubject("Welcome to chat1, today topic: Cultural issues
        // in
        // Brazil");
        // groupChat2.setSubject("Welcome to this room: we are talking today
        // about
        // 2009 meeting");
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
