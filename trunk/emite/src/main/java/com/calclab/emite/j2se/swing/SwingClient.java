/*
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
package com.calclab.emite.j2se.swing;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.j2se.swing.ChatPanel.ChatPanelListener;
import com.calclab.emite.j2se.swing.RoomPanel.RoomPanelListener;
import com.calclab.emite.j2se.swing.roster.RosterPanel;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener2;

public class SwingClient {

    private final ConversationsPanel conversationsPanel;

    private final JPanel root;
    private final JLabel status;
    private final JTabbedPane tabs;
    private final ChatManager chatManager;
    private final RoomManager roomManager;

    public SwingClient(final JFrame frame, final LoginPanel loginPanel, final RosterPanel rosterPanel,
	    final ChatManager chatManager, final RoomManager roomManager) {
	this.chatManager = chatManager;
	this.roomManager = roomManager;

	root = new JPanel(new BorderLayout());
	addXmppListeners();

	final RoomsPanel roomsPanel = new RoomsPanel(new RoomsPanelListener() {
	    public void onRoomEnterd(final String roomName) {
		roomManager.openChat(uri(roomName), null, null);
	    }
	});

	conversationsPanel = new ConversationsPanel();
	status = new JLabel("emite test client");

	root.add(loginPanel, BorderLayout.NORTH);
	root.add(conversationsPanel, BorderLayout.CENTER);
	root.add(status, BorderLayout.SOUTH);

	tabs = new JTabbedPane();
	tabs.add("chats", rosterPanel);
	tabs.add("rooms", roomsPanel);

	root.add(tabs, BorderLayout.EAST);

	frame.setContentPane(root);

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

    private void addXmppListeners() {

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

}
