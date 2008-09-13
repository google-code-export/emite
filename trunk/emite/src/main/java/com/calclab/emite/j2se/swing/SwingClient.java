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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.bosh.Bosh3Settings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.im.client.xold_roster.XRosterItem;
import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.emite.j2se.swing.ChatPanel.ChatPanelListener;
import com.calclab.emite.j2se.swing.LoginPanel.LoginPanelListener;
import com.calclab.emite.j2se.swing.RoomPanel.RoomPanelListener;
import com.calclab.emite.j2se.swing.RosterPanel.RosterPanelListener;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot2;

public class SwingClient {

    private final ConversationsPanel conversationsPanel;
    private final JFrame frame;

    private final LoginPanel loginPanel;

    private final RoomsPanel roomsPanel;
    private final JPanel root;
    private final RosterPanel rosterPanel;
    private final JLabel status;
    private final JTabbedPane tabs;
    private final Session session;
    private final ChatManager chatManager;
    private final RoomManager roomManager;
    private final XRoster xRoster;
    private final XRosterManager xRosterManager;

    public SwingClient(final Connection connection, final Session session, final PresenceManager presenceManager,
	    final XRosterManager xRosterManager, final XRoster xRoster, final ChatManager chatManager,
	    final RoomManager roomManager) {
	this.session = session;
	this.xRosterManager = xRosterManager;
	this.xRoster = xRoster;
	this.chatManager = chatManager;
	this.roomManager = roomManager;

	this.frame = new JFrame("emite swing client");
	root = new JPanel(new BorderLayout());
	addXmppListeners();

	loginPanel = new LoginPanel(new LoginPanelListener() {
	    public void onLogin(final String httpBase, final String domain, final String userName, String password) {
		final String resource = "emite-swing";
		connection.setSettings(new Bosh3Settings(httpBase, domain));
		XmppURI uri;
		if ("anonymous".equals(userName)) {
		    uri = Session.ANONYMOUS;
		    password = null;
		} else {
		    uri = XmppURI.uri(userName, domain, resource);
		}
		session.login(uri, password);
		presenceManager.setOwnPresence(Presence.build("do not disturb at: " + new Date().toString(),
			Presence.Show.dnd));
	    }

	    public void onLogout() {
		session.logout();
	    }

	});

	loginPanel.addConfiguration(new ConnectionConfiguration("empty", "", "", "", ""));
	loginPanel.addConfiguration(new ConnectionConfiguration("admin @ local openfire",
		"http://localhost:5280/http-bind/", "localhost", "admin", "easyeasy"));
	loginPanel.addConfiguration(new ConnectionConfiguration("dani @ local ejabberd",
		"http://localhost:5280/http-bind/", "localhost", "dani", "dani"));
	loginPanel.addConfiguration(new ConnectionConfiguration("dani @ emite demo",
		"http://emite.ourproject.org/proxy", "emitedemo.ourproject.org", "dani", "dani"));
	loginPanel.addConfiguration(new ConnectionConfiguration("test1 @ jetty proxy",
		"http://localhost:4444/http-bind", "localhost", "test1", "test1"));
	loginPanel.addConfiguration(new ConnectionConfiguration("test1 @ jetty bosh servlet",
		"http://emite.ourproject.org/proxy", "localhost", "test1", "test1"));

	rosterPanel = new RosterPanel(frame, new RosterPanelListener() {
	    public void onAddRosterItem(final String uri, final String name) {
		xRosterManager.requestAddItem(uri(uri), name, null);
	    }

	    public void onRemoveItem(final XRosterItem item) {
		xRosterManager.requestRemoveItem(item.getJID());
	    }

	    public void onStartChat(final XRosterItem item) {
		chatManager.openChat(item.getJID(), null, null);
	    }
	});

	roomsPanel = new RoomsPanel(new RoomsPanelListener() {
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

    }

    public void start() {
	frame.setContentPane(root);
	frame.setSize(900, 400);
	frame.setVisible(true);
	frame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(final WindowEvent e) {
		session.logout();
		System.exit(0);
	    }
	});
    }

    protected void addChatListener(final Chat chat, final ChatPanel chatPanel) {
	chat.onMessageReceived(new Slot<Message>() {
	    public void onEvent(final Message message) {
		chatPanel.showIcomingMessage(message.getFromAsString(), message.getBody());
	    }
	});
	chat.onMessageSent(new Slot<Message>() {
	    public void onEvent(final Message message) {
		chatPanel.showOutMessage(message.getBody());
	    }
	});

    }

    private void addXmppListeners() {
	session.onStateChanged(new Slot<Session.State>() {
	    public void onEvent(final Session.State current) {
		print("STATE: " + current);
		final boolean isConnected = current == Session.State.ready;
		loginPanel.showState("state: " + current.toString(), isConnected);
		tabs.setEnabled(isConnected);
		if (current == Session.State.disconnected) {
		    rosterPanel.clear();
		} else if (current == Session.State.notAuthorized) {
		    JOptionPane.showMessageDialog(frame, "lo siento, tienes mal la contraseña -o el usuario ;)-");
		}
	    }
	});

	chatManager.onChatCreated(new Slot<Chat>() {
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

	chatManager.onChatClosed(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		conversationsPanel.close(chat.getID());
	    }
	});

	roomManager.onChatCreated(new Slot<Chat>() {
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

		room.onMessageReceived(new Slot<Message>() {
		    public void onEvent(final Message message) {
			roomPanel.showIcomingMessage(message.getFromAsString(), message.getBody());
		    }
		});

		room.onMessageSent(new Slot<Message>() {
		    public void onEvent(final Message message) {
			roomPanel.showOutMessage(message.getBody());
		    }
		});

		room.onOccupantsChanged(new Slot<Collection<Occupant>>() {
		    public void onEvent(final Collection<Occupant> users) {
			roomPanel.setUsers(users);
		    }
		});

		room.onSubjectChanged(new Slot2<Occupant, String>() {
		    public void onEvent(final Occupant occupant, final String newSubject) {
			final String nick = occupant != null ? occupant.getNick() : "";
			roomPanel.showIcomingMessage(nick, "New subject: " + newSubject);
		    }
		});

	    }
	});

	roomManager.onChatClosed(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		conversationsPanel.close(chat.getID());
	    }
	});

	roomManager.onInvitationReceived(new Slot<RoomInvitation>() {
	    public void onEvent(final RoomInvitation invitation) {
		roomManager.openChat(invitation.getRoomURI(), null, null);
	    }
	});

	xRoster.onItemChanged(new Slot<XRosterItem>() {
	    public void onEvent(final XRosterItem item) {
		print("ROSTER ITEM PRESENCE CHANGED");
		rosterPanel.refresh();
	    }
	});
	xRoster.onRosterChanged(new Slot<Collection<XRosterItem>>() {

	    public void onEvent(final Collection<XRosterItem> items) {
		print("ROSTER INITIALIZED");
		rosterPanel.clear();
		for (final XRosterItem item : items) {
		    rosterPanel.add(item.getName(), item);
		}
	    }
	});

	xRosterManager.onSubscriptionRequested(new Slot<Presence>() {
	    public void onEvent(final Presence presence) {
		final Object message = presence.getFromAsString() + " want to add you to his/her roster. Accept?";
		final int result = JOptionPane.showConfirmDialog(frame, message);
		if (result == JOptionPane.OK_OPTION) {
		    xRosterManager.acceptSubscription(presence);
		}
		print("SUBSCRIPTION: " + presence);
	    }
	});

	session.onPresence(new Slot<Presence>() {
	    public void onEvent(final Presence presence) {
		print("PRESENCE!!: " + presence);
	    }
	});

    }

    private void print(final String message) {
	Log.info(message);
	if (status != null) {
	    status.setText(message);
	}
    }
}
