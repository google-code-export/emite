/**
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.examplechat.client.chatuiplugin.utils;


public class MultiChatSamples {
    // public static void show(final DefaultDispatcher dispatcher, final String
    // currentUserJid) {
    //
    // final Presence presenceForTest = new Presence();
    // presenceForTest.setShow(Presence.Show.available);
    // presenceForTest.setType(Presence.Type.available.toString());
    // presenceForTest.setStatus("I\'m out for dinner");
    //
    // final String chatId1 = "ma@example.com";
    // final PairChatUser pairUser = new PairChatUser("images/person-def.gif",
    // XmppURI.parse(chatId1), "Mark",
    // "red", presenceForTest);
    // dispatcher.fire(ChatDialogPlugin.CREATE_PAIR_CHAT, pairUser);
    // dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new
    // ChatMessageParam(XmppURI.parse(chatId1), XmppURI
    // .parse(chatId1), "hello"));
    // dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new
    // ChatMessageParam(XmppURI.parse(chatId1), XmppURI
    // .parse(currentUserJid), "hi"));
    //
    // final String chatId2 = "fran@example.com";
    // final PairChatUser pairUser2 = new PairChatUser("images/person-def.gif",
    // XmppURI.parse(chatId2), "frank",
    // "cyan", presenceForTest);
    // dispatcher.fire(ChatDialogPlugin.CREATE_PAIR_CHAT, pairUser2);
    // dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new
    // ChatMessageParam(XmppURI.parse(chatId2), XmppURI
    // .parse(chatId2), "hello"));
    // dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new
    // ChatMessageParam(XmppURI.parse(chatId2), XmppURI
    // .parse(currentUserJid), "hi"));
    //
    // sendTestMessages(dispatcher, chatId1, currentUserJid, chatId1, 3);
    // sendTestMessages(dispatcher, chatId2, chatId2, currentUserJid, 3);
    //
    // final String groupChatId1 = "chat1@rooms.localhost";
    // final String groupChatUserAlias1 = "luther.b";
    // dispatcher.fire(ChatDialogPlugin.CREATE_GROUP_CHAT, new
    // CreateGroupChatActionParam(groupChatId1,
    // groupChatUserAlias1, GroupChatUser.MODERADOR));
    // final String groupChatId2 = "chat2@rooms.localhost";
    // final String groupChatUserAlias2 = "luther";
    // dispatcher.fire(ChatDialogPlugin.CREATE_GROUP_CHAT, new
    // CreateGroupChatActionParam(groupChatId2,
    // groupChatUserAlias2, GroupChatUser.PARTICIPANT));
    //
    // dispatcher.fire(ChatDialogPlugin.SET_GROUPCHAT_SUBJECT, new
    // GroupChatSubjectParam(new Chat(XmppURI
    // .parse(groupChatId1)), "Welcome to chat1, today topic: Cultural issues in
    // Brazil"));
    //
    // // groupChat2.setSubject("Welcome to this room: we are talking today
    // // about
    // // 2009 meeting");
    //
    // final GroupChatUser lutherb = new
    // GroupChatUser(XmppURI.parse("groucho@localhost"), groupChatUserAlias1,
    // "blue", GroupChatUser.MODERADOR);
    // final GroupChatUser luther = new
    // GroupChatUser(XmppURI.parse("luther@localhost"), groupChatUserAlias2,
    // "grey", GroupChatUser.MODERADOR);
    // final String groupChatUserAlias3 = "ouser";
    // final GroupChatUser otherUser = new
    // GroupChatUser(XmppURI.parse("otheruser@exammple.com"),
    // groupChatUserAlias3, "red", GroupChatUser.MODERADOR);
    // final GroupChatUser otherUser2 = new
    // GroupChatUser(XmppURI.parse("otheruser2@example.com"),
    // groupChatUserAlias3, "green", GroupChatUser.MODERADOR);
    //
    // dispatcher
    // .fire(ChatDialogPlugin.ADD_USER_TO_GROUP_CHAT, new
    // GroupChatUserAddActionParam(groupChatId1, lutherb));
    // dispatcher.fire(ChatDialogPlugin.ADD_USER_TO_GROUP_CHAT, new
    // GroupChatUserAddActionParam(groupChatId1,
    // otherUser));
    // dispatcher.fire(ChatDialogPlugin.ADD_USER_TO_GROUP_CHAT, new
    // GroupChatUserAddActionParam(groupChatId2, luther));
    // dispatcher.fire(ChatDialogPlugin.ADD_USER_TO_GROUP_CHAT, new
    // GroupChatUserAddActionParam(groupChatId2,
    // otherUser2));
    //
    // dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new
    // ChatMessageParam(XmppURI.parse(groupChatId1), XmppURI
    // .parse(groupChatUserAlias1), "testing"));
    // dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new
    // ChatMessageParam(XmppURI.parse(groupChatId1), XmppURI
    // .parse(groupChatUserAlias3), "testing"));
    //
    // sendTestMessages(dispatcher, groupChatId1, groupChatUserAlias1,
    // groupChatUserAlias3, 3);
    // sendTestMessages(dispatcher, groupChatId2, groupChatUserAlias3,
    // groupChatUserAlias2, 3);
    //
    // // Log.debug("Adding other messages");
    // // groupChat2.addInfoMessage("Mensaje de evento en group chat 2");
    // // groupChat2.addDelimiter("17:35");
    //
    // sendTestMessages(dispatcher, chatId1, currentUserJid, chatId1, 3);
    // sendTestMessages(dispatcher, chatId2, chatId2, currentUserJid, 3);
    //
    // }
    //
    // private static void sendTestMessages(final DefaultDispatcher dispatcher,
    // final String chatId, final String userId1,
    // final String userId2, final int num) {
    // for (int i = 0; i < num; i++) {
    // dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new
    // ChatMessageParam(XmppURI.parse(chatId), XmppURI
    // .parse(userId1), "hello " + userId2 + " this is only a test ;)"));
    // dispatcher.fire(ChatDialogPlugin.MESSAGE_RECEIVED, new
    // ChatMessageParam(XmppURI.parse(chatId), XmppURI
    // .parse(userId2), "hello " + userId1 + " this is also only a test :)"));
    // }
    // }
}
