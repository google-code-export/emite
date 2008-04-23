package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.DefaultContainer;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.extra.muc.MUCModule;
import com.calclab.emite.client.extra.muc.Occupant;
import com.calclab.emite.client.extra.muc.Room;
import com.calclab.emite.client.extra.muc.RoomListener;
import com.calclab.emite.client.extra.muc.RoomManager;
import com.calclab.emite.client.extra.muc.RoomManagerListener;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.j2se.services.HttpConnectorListener;
import com.calclab.emite.j2se.services.J2SEPlugin;
import com.calclab.emite.j2se.swing.ChatPanel.ChatPanelListener;
import com.calclab.emite.j2se.swing.LoginPanel.LoginPanelListener;
import com.calclab.emite.j2se.swing.RoomPanel.RoomPanelListener;
import com.calclab.emite.j2se.swing.RosterPanel.RosterPanelListener;

public class SwingClient {

    public static void main(final String args[]) {
	new SwingClient(new JFrame("emite swing client")).start();
    }
    private final ConversationsPanel conversationsPanel;
    private final JFrame frame;

    private final LoginPanel loginPanel;

    private final RoomsPanel roomsPanel;
    private final JPanel root;
    private final RosterPanel rosterPanel;
    private final JLabel status;
    private final JTabbedPane tabs;
    private Xmpp xmpp;

    public SwingClient(final JFrame frame) {
	this.frame = frame;
	root = new JPanel(new BorderLayout());

	loginPanel = new LoginPanel(new LoginPanelListener() {
	    public void onLogin(final String httpBase, final String domain, final String userName, final String password) {
		final String resource = "emite-swing";
		createXMPP(httpBase);
		xmpp.login(new XmppURI(userName, domain, resource), password, null, "hola!");
	    }

	    public void onLogout() {
		xmpp.logout();
		xmpp = null;
	    }

	});
	rosterPanel = new RosterPanel(frame, new RosterPanelListener() {
	    public void onAddRosterItem(final String uri, final String name) {
		xmpp.getRosterManager().requestAddItem(XmppURI.parse(uri), name, null);
	    }

	    public void onRemoveItem(final RosterItem item) {
		xmpp.getRosterManager().requestRemoveItem(item.getJID());
	    }

	    public void onStartChat(final RosterItem item) {
		xmpp.getChatManager().openChat(item.getJID());
	    }
	});

	roomsPanel = new RoomsPanel(new RoomsPanelListener() {
	    public void onRoomEnterd(final String roomName) {
		final RoomManager roomManager = MUCModule.getRoomManager(xmpp.getComponents());
		roomManager.openChat(XmppURI.parse(roomName));
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

    private void createXMPP(final String httpbase) {
	final Container container = J2SEPlugin.install(new DefaultContainer(), new HttpConnectorListener() {
	    public void onError(final String id, final String cause) {
		System.out.println("CONN # " + id + "-ERROR: " + cause);
	    }

	    public void onFinish(final String id, final long duration) {
	    }

	    public void onResponse(final String id, final String response) {
		System.out.println("IN: " + response);
	    }

	    public void onSend(final String id, final String xml) {
		System.out.println("OUT: " + xml);
	    }

	    public void onStart(final String id) {
	    }

	});
	this.xmpp = new Xmpp(container, new BoshOptions(httpbase));

	xmpp.getSession().addListener(new SessionListener() {
	    public void onStateChanged(final State old, final State current) {
		print("STATE: " + current);
		final boolean isConnected = current == State.connected;
		loginPanel.showState("state: " + current.toString(), isConnected);
		tabs.setEnabled(isConnected);
		if (current == State.disconnected) {
		    rosterPanel.clear();
		} else if (current == State.notAuthorized) {
		    JOptionPane.showMessageDialog(frame, "lo siento, tienes mal la contraseña -o el usuario ;)-");
		}
	    }
	});
	xmpp.getChatManager().addListener(new ChatManagerListener() {
	    public void onChatClosed(final Chat chat) {
		conversationsPanel.close(chat.getID());
	    }

	    public void onChatCreated(final Chat chat) {
		final ChatPanel chatPanel = conversationsPanel.createChat(chat.getOtherURI().toString(), chat.getID(),
			new ChatPanelListener() {
			    public void onClose(final ChatPanel source) {
				xmpp.getChatManager().close(chat);

			    }

			    public void onSend(final ChatPanel source, final String text) {
				chat.send(text);
				source.clearMessage();
			    }

			});
		chat.addListener(new ChatListener() {
		    public void onMessageReceived(final Chat chat, final Message message) {
			chatPanel.showIcomingMessage(message.getFrom(), message.getBody());
		    }

		    public void onMessageSent(final Chat chat, final Message message) {
			chatPanel.showOutMessage(message.getBody());
		    }
		});
		chatPanel.clearMessage();
	    }
	});

	final RoomManager roomManager = MUCModule.getRoomManager(xmpp.getComponents());
	roomManager.addListener(new RoomManagerListener() {
	    public void onChatClosed(final Chat chat) {
		conversationsPanel.close(chat.getID());
	    }

	    public void onChatCreated(final Chat room) {
		final RoomPanel roomPanel = conversationsPanel.createRoom(room.getOtherURI(), room.getID(),
			new RoomPanelListener() {
			    public void onClose(final ChatPanel source) {
				MUCModule.getRoomManager(xmpp.getComponents()).close(room);
			    }

			    public void onInviteUser(final String userJid, final String reasonText) {
				((Room) room).sendInvitationTo(userJid, reasonText);
			    }

			    public void onModifySubject(final String newSubject) {
				((Room) room).setSubject(newSubject);
			    }

			    public void onSend(final ChatPanel source, final String text) {
				room.send(text);
				source.clearMessage();
			    }
			});
		room.addListener(new RoomListener() {
		    public void onMessageReceived(final Chat chat, final Message message) {
			roomPanel.showIcomingMessage(message.getFrom(), message.getBody());
		    }

		    public void onMessageSent(final Chat chat, final Message message) {
			roomPanel.showOutMessage(message.getBody());
		    }

		    public void onOccupantModified(final Occupant occupant) {
		    }

		    public void onOccupantsChanged(final Collection<Occupant> users) {
			roomPanel.setUsers(users);
		    }

		    public void onSubjectSet(final String nick, final String newSubject) {
		    }
		});
	    }

	    public void onInvitationReceived(final XmppURI invitor, final XmppURI roomURI, final String reason) {
		roomManager.openChat(roomURI);
	    }
	});

	xmpp.getRoster().addListener(new RosterListener() {
	    public void onItemChanged(final RosterItem item) {
		print("ROSTER ITEM PRESENCE CHANGED");
		rosterPanel.refresh();
	    }

	    public void onRosterChanged(final Collection<RosterItem> items) {
		print("ROSTER INITIALIZED");
		rosterPanel.clear();
		for (final RosterItem item : items) {
		    rosterPanel.add(item.getName(), item);
		}
	    }
	});
	xmpp.getPresenceManager().addListener(new PresenceListener() {
	    public void onPresenceReceived(final Presence presence) {
		print("PRESENCE!!: " + presence);
	    }

	    public void onSubscribedReceived(final Presence presence) {
	    }

	    public void onSubscriptionRequest(final Presence presence) {
		final Object message = presence.getFrom() + " want to add you to his/her roster. Accept?";
		final int result = JOptionPane.showConfirmDialog(frame, message);
		if (result == JOptionPane.OK_OPTION) {
		    xmpp.getPresenceManager().acceptSubscription(presence);
		}
		print("SUBSCRIPTION: " + presence);
	    }

	    public void onUnsubscribedReceived(final Presence presence) {
	    }
	});

    }

    private void print(final String message) {
	Log.info(message);
	status.setText(message);
    }

    private void start() {
	frame.setContentPane(root);
	frame.setSize(900, 400);
	frame.setVisible(true);
	frame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(final WindowEvent e) {
		if (xmpp != null) {
		    xmpp.stop();
		}
		System.exit(0);
	    }
	});
    }
}
