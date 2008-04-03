package com.calclab.emite.client.swing;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.TestHelper;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.connector.HttpConnectorListener;
import com.calclab.emite.client.im.chat.ChatDefault;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.swing.ChatPanel.ChatPanelListener;
import com.calclab.emite.client.swing.LoginPanel.LoginPanelListener;
import com.calclab.emite.client.swing.RosterPanel.RosterPanelListener;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;

public class SwingClient {

    public static void main(final String args[]) {
	new SwingClient().start();
    }

    private final ConversationsPanel conversationsPanel;
    private final LoginPanel loginPanel;

    private final JPanel root;

    private final RosterPanel rosterPanel;
    private Xmpp xmpp;

    public SwingClient() {
	root = new JPanel(new BorderLayout());

	loginPanel = new LoginPanel(new LoginPanelListener() {
	    public void onLogin(final String httpBase, final String domain, final String userName, final String password) {
		xmpp.login(userName, password);
	    }

	    public void onLogout() {
		xmpp.logout();
	    }

	});
	rosterPanel = new RosterPanel(new RosterPanelListener() {
	    public void onStartChat(final RosterItem item) {
		xmpp.getChat().newChat(item.getXmppURI());
	    }
	});
	conversationsPanel = new ConversationsPanel();
	root.add(loginPanel, BorderLayout.NORTH);
	root.add(rosterPanel, BorderLayout.EAST);
	root.add(conversationsPanel, BorderLayout.CENTER);

	initXMPP();
    }

    private void initXMPP() {
	this.xmpp = TestHelper.createXMPP(new HttpConnectorListener() {
	    public void onError(final String id, final String cause) {
		print((id + "-ERROR: " + cause));
	    }

	    public void onFinish(final String id, final long duration) {
		print((id + "-FINISH " + duration + "ms"));
	    }

	    public void onResponse(final String id, final String response) {
		print((id + "-RESPONSE: " + response));
	    }

	    public void onSend(final String id, final String xml) {
		print((id + "-SENDING: " + xml));
	    }

	    public void onStart(final String id) {
		print((id + "-STARTED: " + id));
	    }

	});
	xmpp.getSession().addListener(new SessionListener() {
	    public void onStateChanged(final State old, final State current) {
		print("STATE: " + current);
		loginPanel.showState("state: " + current.toString(), current == State.connected);
	    }
	});
	xmpp.getChat().addListener(new ChatManagerListener() {
	    public void onChatCreated(final ChatDefault chatDefault) {
		final ChatPanel chatPanel = conversationsPanel.createChat(chatDefault.getID(), new ChatPanelListener() {
		    public void onSend(ChatPanel source, String text) {
			chatDefault.send(text);
			source.clearMessage();
		    }

		});
		chatDefault.addListener(new ChatListener() {
		    public void onMessageReceived(final ChatDefault chatDefault, final Message message) {
			chatPanel.showIcomingMessage(message.getFrom(), message.getBody());
		    }

		    public void onMessageSent(final ChatDefault chatDefault, final Message message) {
			chatPanel.showOutMessage(message.getBody());
		    }
		});
		chatPanel.clearMessage();
	    }
	});
	xmpp.getRoster().addListener(new RosterListener() {
	    public void onRosterInitialized(final List<RosterItem> items) {
		print("ROSTER INITIALIZED");
		for (final RosterItem item : items) {
		    rosterPanel.add(item.getName(), item);
		}
	    }
	});
	xmpp.getPresenceManager().addListener(new PresenceListener() {
	    public void onPresenceReceived(final Presence presence) {
		print("PRESENCE!!: " + presence);
	    }

	    public void onSubscriptionRequest(final Presence presence) {
		print("SUBSCRIPTION: " + presence);
	    }
	});
    }

    private void print(final String message) {
	Log.info(message);
    }

    private void start() {
	final JFrame frame = new JFrame("emite swing client");
	frame.setContentPane(root);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(600, 400);
	frame.setVisible(true);
    }
}
