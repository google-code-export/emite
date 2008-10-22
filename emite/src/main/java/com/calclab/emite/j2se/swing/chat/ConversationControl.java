package com.calclab.emite.j2se.swing.chat;

import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.j2se.swing.roster.RosterPanel;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.listener.Listener;

public class ConversationControl {

    public ConversationControl(final ChatManager chatManager, final RoomManager roomManager,
	    final RosterPanel rosterPanel, final ConversationsPanel conversationsPanel) {

	rosterPanel.onStartChat(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		chatManager.openChat(item.getJID(), null, null);
	    }
	});

	chatManager.onChatCreated(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		final ChatPanel chatPanel = conversationsPanel.createChat(chatManager, conversation);
		chatPanel.clearMessage();
	    }
	});

	chatManager.onChatClosed(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		conversationsPanel.close(conversation.getID());
	    }
	});

	roomManager.onChatCreated(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		final Room room = (Room) conversation;
		final RoomPanel roomPanel = conversationsPanel.createRoomPanel(roomManager, room);
		roomPanel.clearMessage();
		roomPanel.showIcomingMessage(null, "The room is " + room.getState().toString());
	    }
	});

	roomManager.onChatClosed(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		conversationsPanel.close(conversation.getID());
	    }
	});

	roomManager.onInvitationReceived(new Listener<RoomInvitation>() {
	    public void onEvent(final RoomInvitation invitation) {
		roomManager.openChat(invitation.getRoomURI(), null, null);
	    }
	});
    }

}
