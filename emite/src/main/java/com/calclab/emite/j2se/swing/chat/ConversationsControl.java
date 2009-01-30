package com.calclab.emite.j2se.swing.chat;

import javax.swing.JOptionPane;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.j2se.swing.roster.RosterPanel;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.events.Listener;

public class ConversationsControl {

    public ConversationsControl(final ChatManager chatManager, final RoomManager roomManager,
	    final RosterPanel rosterPanel, final ConversationsPanel conversationsPanel) {

	rosterPanel.onStartChat(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		chatManager.openChat(item.getJID());
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
		final String nickName = JOptionPane
			.showInputDialog("You have been invited to a room. Please specify a nick");
		if (nickName != null) {
		    final XmppURI roomURI = invitation.getRoomURI();
		    final XmppURI jid = XmppURI.uri(roomURI.getNode(), roomURI.getHost(), nickName);
		    roomManager.openChat(jid);
		}
	    }
	});
    }

}
