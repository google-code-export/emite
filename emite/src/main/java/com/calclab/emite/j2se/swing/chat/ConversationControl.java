package com.calclab.emite.j2se.swing.chat;

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.j2se.swing.RoomPanel;
import com.calclab.emite.j2se.swing.RoomPanel.RoomPanelListener;
import com.calclab.emite.j2se.swing.chat.ChatPanel.ChatPanelListener;
import com.calclab.emite.j2se.swing.roster.RosterPanel;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener2;

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
		final ChatPanel chatPanel = conversationsPanel.createChat(chat.getOtherURI().toString(), chat.getID(),
			new ChatPanelListener() {
			    public void onClose(final ChatPanel source) {
				chatManager.close(chat);

			    }

			    public void onSend(final ChatPanel source, final String text) {
				chat.send(new Message(text));
				source.clearMessage();
			    }

			});
		addChatListener(chat, chatPanel);
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
		final RoomPanel roomPanel = conversationsPanel.createRoom(room.getOtherURI(), room.getID(),
			new RoomPanelListener() {
			    public void onClose(final ChatPanel source) {
				roomManager.close(room);
			    }

			    public void onInviteUser(final String userJid, final String reasonText) {
				(room).sendInvitationTo(userJid, reasonText);
			    }

			    public void onModifySubject(final String newSubject) {
				(room).setSubject(newSubject);
			    }

			    public void onSend(final ChatPanel source, final String text) {
				room.send(new Message(text));
				source.clearMessage();
			    }
			});
		addChatListener(room, roomPanel);

		room.onMessageReceived(new Listener<Message>() {
		    public void onEvent(final Message message) {
			roomPanel.showIcomingMessage(message.getFromAsString(), message.getBody());
		    }
		});

		room.onMessageSent(new Listener<Message>() {
		    public void onEvent(final Message message) {
			roomPanel.showOutMessage(message.getBody());
		    }
		});

		room.onOccupantsChanged(new Listener<Collection<Occupant>>() {
		    public void onEvent(final Collection<Occupant> users) {
			roomPanel.setUsers(users);
		    }
		});

		room.onSubjectChanged(new Listener2<Occupant, String>() {
		    public void onEvent(final Occupant occupant, final String newSubject) {
			final String nick = occupant != null ? occupant.getNick() : "";
			roomPanel.showIcomingMessage(nick, "New subject: " + newSubject);
		    }
		});

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

    protected void addChatListener(final Chat chat, final ChatPanel chatPanel) {
	chat.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		chatPanel.showIcomingMessage(message.getFromAsString(), message.getBody());
	    }
	});
	chat.onMessageSent(new Listener<Message>() {
	    public void onEvent(final Message message) {
		chatPanel.showOutMessage(message.getBody());
	    }
	});
    }

}
