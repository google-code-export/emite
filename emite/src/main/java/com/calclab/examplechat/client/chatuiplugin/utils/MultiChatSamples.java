package com.calclab.examplechat.client.chatuiplugin.utils;

import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppJID;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogPlugin;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.ChatId;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.ChatMessageParam;
import com.calclab.examplechat.client.chatuiplugin.params.CreateGroupChatActionParam;
import com.calclab.examplechat.client.chatuiplugin.params.GroupChatSubjectParam;
import com.calclab.examplechat.client.chatuiplugin.params.GroupChatUserAddActionParam;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;

public class MultiChatSamples {
    public static void show(final DefaultDispatcher dispatcher, final String currentUserJid) {

        Presence presenceForTest = new Presence();
        presenceForTest.setShow(Presence.Show.available);
        presenceForTest.setType(Presence.Type.available.toString());
        presenceForTest.setStatus("I\'m out for dinner");

        String chatId1 = "ma@example.com";
        PairChatUser pairUser = new PairChatUser("images/person-def.gif", XmppJID.parseJID(chatId1), "Mark", "red",
                presenceForTest);
        dispatcher.fire(ChatDialogPlugin.CREATE_PAIR_CHAT, pairUser);
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new ChatMessageParam(XmppJID.parseJID(chatId1), XmppJID
                .parseJID(chatId1), "hello"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new ChatMessageParam(XmppJID.parseJID(chatId1), XmppJID
                .parseJID(currentUserJid), "hi"));

        String chatId2 = "fran@example.com";
        PairChatUser pairUser2 = new PairChatUser("images/person-def.gif", XmppJID.parseJID(chatId2), "frank", "cyan",
                presenceForTest);
        dispatcher.fire(ChatDialogPlugin.CREATE_PAIR_CHAT, pairUser2);
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new ChatMessageParam(XmppJID.parseJID(chatId2), XmppJID
                .parseJID(chatId2), "hello"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new ChatMessageParam(XmppJID.parseJID(chatId2), XmppJID
                .parseJID(currentUserJid), "hi"));

        sendTestMessages(dispatcher, chatId1, currentUserJid, chatId1, 3);
        sendTestMessages(dispatcher, chatId2, chatId2, currentUserJid, 3);

        String groupChatId1 = "chat1@rooms.localhost";
        String groupChatUserAlias1 = "luther.b";
        dispatcher.fire(ChatDialogPlugin.CREATE_GROUP_CHAT, new CreateGroupChatActionParam(groupChatId1,
                groupChatUserAlias1, GroupChatUser.MODERADOR));
        String groupChatId2 = "chat2@rooms.localhost";
        String groupChatUserAlias2 = "luther";
        dispatcher.fire(ChatDialogPlugin.CREATE_GROUP_CHAT, new CreateGroupChatActionParam(groupChatId2,
                groupChatUserAlias2, GroupChatUser.PARTICIPANT));

        dispatcher.fire(ChatDialogPlugin.SET_GROUPCHAT_SUBJECT, new GroupChatSubjectParam(new ChatId(XmppJID
                .parseJID(groupChatId1)), "Welcome to chat1, today topic: Cultural issues in Brazil"));

        // groupChat2.setSubject("Welcome to this room: we are talking today
        // about
        // 2009 meeting");

        GroupChatUser lutherb = new GroupChatUser(XmppJID.parseJID("groucho@localhost"), groupChatUserAlias1, "blue",
                GroupChatUser.MODERADOR);
        GroupChatUser luther = new GroupChatUser(XmppJID.parseJID("luther@localhost"), groupChatUserAlias2, "grey",
                GroupChatUser.MODERADOR);
        String groupChatUserAlias3 = "ouser";
        GroupChatUser otherUser = new GroupChatUser(XmppJID.parseJID("otheruser@exammple.com"), groupChatUserAlias3,
                "red", GroupChatUser.MODERADOR);
        GroupChatUser otherUser2 = new GroupChatUser(XmppJID.parseJID("otheruser2@example.com"), groupChatUserAlias3,
                "green", GroupChatUser.MODERADOR);

        dispatcher
                .fire(ChatDialogPlugin.ADD_USER_TO_GROUP_CHAT, new GroupChatUserAddActionParam(groupChatId1, lutherb));
        dispatcher.fire(ChatDialogPlugin.ADD_USER_TO_GROUP_CHAT, new GroupChatUserAddActionParam(groupChatId1,
                otherUser));
        dispatcher.fire(ChatDialogPlugin.ADD_USER_TO_GROUP_CHAT, new GroupChatUserAddActionParam(groupChatId2, luther));
        dispatcher.fire(ChatDialogPlugin.ADD_USER_TO_GROUP_CHAT, new GroupChatUserAddActionParam(groupChatId2,
                otherUser2));

        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new ChatMessageParam(XmppJID.parseJID(groupChatId1), XmppJID
                .parseJID(groupChatUserAlias1), "testing"));
        dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new ChatMessageParam(XmppJID.parseJID(groupChatId1), XmppJID
                .parseJID(groupChatUserAlias3), "testing"));

        sendTestMessages(dispatcher, groupChatId1, groupChatUserAlias1, groupChatUserAlias3, 3);
        sendTestMessages(dispatcher, groupChatId2, groupChatUserAlias3, groupChatUserAlias2, 3);

        // Log.debug("Adding other messages");
        // groupChat2.addInfoMessage("Mensaje de evento en group chat 2");
        // groupChat2.addDelimiter("17:35");

        sendTestMessages(dispatcher, chatId1, currentUserJid, chatId1, 3);
        sendTestMessages(dispatcher, chatId2, chatId2, currentUserJid, 3);

    }

    private static void sendTestMessages(final DefaultDispatcher dispatcher, final String chatId, final String userId1,
            final String userId2, final int num) {
        for (int i = 0; i < num; i++) {
            dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new ChatMessageParam(XmppJID.parseJID(chatId), XmppJID
                    .parseJID(userId1), "hello " + userId2 + " this is only a test ;)"));
            dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new ChatMessageParam(XmppJID.parseJID(chatId), XmppJID
                    .parseJID(userId2), "hello " + userId1 + " this is also only a test :)"));
        }
    }
}
