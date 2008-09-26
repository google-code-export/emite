package com.calclab.emite.j2se.swing.chat;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.j2se.swing.roster.RosterPanel;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.listener.Listener;

public class ConversationControl {

    public ConversationControl(final ChatManager chatManager, final RoomManager roomManager,
	    final RosterPanel rosterPanel, final ConversationsPanel conversationsPanel) {

	rosterPanel.onStartChat(new Listener<XmppURI>() {
	    public void onEvent(final XmppURI parameter) {
		chatManager.openChat(parameter, null, null);
	    }
	});

	chatManager.onChatCreated(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		final ChatPanel chatPanel = conversationsPanel.createChat(chatManager, chat);
		chatPanel.clearMessage();
	    }
	});

	chatManager.onChatClosed(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		conversationsPanel.close(chat.getID());
	    }
	});

	roomManager.onChatCreated(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		final Room room = (Room) chat;
		final RoomPanel roomPanel = conversationsPanel.createRoomPanel(roomManager, room);
		roomPanel.clearMessage();
		roomPanel.showIcomingMessage(null, "The room is " + room.getState().toString());
	    }
	});

	roomManager.onChatClosed(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		conversationsPanel.close(chat.getID());
	    }
	});

	roomManager.onInvitationReceived(new Listener<RoomInvitation>() {
	    public void onEvent(final RoomInvitation invitation) {
		roomManager.openChat(invitation.getRoomURI(), null, null);
	    }
	});
    }

}
