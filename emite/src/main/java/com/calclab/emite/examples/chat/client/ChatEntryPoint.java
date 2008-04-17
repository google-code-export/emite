package com.calclab.emite.examples.chat.client;

import java.util.Collection;
import java.util.HashMap;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.examples.chat.client.ChatPanel.ChatPanelListener;
import com.calclab.emite.examples.chat.client.ConversationsPanel.ConversationsListener;
import com.calclab.emite.examples.chat.client.LoginPanel.LoginPanelListener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;

public class ChatEntryPoint implements EntryPoint {

    private HashMap<XmppURI, ChatPanel> chats;
    private ConversationsPanel conversationsPanel;
    private DialogBox dialogBox;
    private LoginPanel loginPanel;
    private Xmpp xmpp;

    public void onModuleLoad() {
	chats = new HashMap<XmppURI, ChatPanel>();

	conversationsPanel = new ConversationsPanel(new ConversationsListener() {
	    public void onBeginChat(final String jid) {
		final Chat chat = xmpp.getChatManager().openChat(XmppURI.parse(jid));
		final ChatPanel chatPanel = chats.get(chat.getOtherURI());
		conversationsPanel.show(jid, chatPanel);
	    }

	    public void onLogout() {
		xmpp.logout();
		conversationsPanel.clearRoster();
		dialogBox.show();
	    }
	});

	dialogBox = new DialogBox();
	loginPanel = new LoginPanel(new LoginPanelListener() {
	    public void onLogin(final String bind, final String domain, final String name, final String password) {
		loginPanel.setStatus("preparing...");
		createXMPP(bind);
		xmpp.login(new XmppURI(name, domain, "emite"), password, null, null);
	    }
	});
	dialogBox.setText("Login");
	dialogBox.setWidget(loginPanel);

	RootPanel.get().add(conversationsPanel);

	dialogBox.center();
	dialogBox.show();
    }

    private ChatPanel createChatPanel(final XmppURI uri, final Chat chat) {
	final ChatPanel chatPanel = new ChatPanel(new ChatPanelListener() {
	    public void onSend(final String text) {
		chat.send(text);
	    }
	});
	chat.addListener(new ChatListener() {
	    public void onMessageReceived(final Chat chat, final Message message) {
		chatPanel.showIncomingMessage(message.getFromURI(), message.getBody());
	    }

	    public void onMessageSent(final Chat chat, final Message message) {
		chatPanel.showOutcomingMessage(message.getBody());
	    }
	});
	chats.put(uri, chatPanel);
	conversationsPanel.addChat(uri.toString(), chatPanel);
	return chatPanel;
    }

    private void createXMPP(final String bind) {
	xmpp = Xmpp.create(new BoshOptions(bind));
	xmpp.getSession().addListener(new SessionListener() {
	    public void onStateChanged(final State old, final State current) {
		final String theStatus = current.toString();
		loginPanel.setStatus(theStatus);
		conversationsPanel.setStatus(theStatus);
		switch (current) {
		case connected:
		    dialogBox.hide();
		    break;
		}
	    }
	});

	xmpp.getChatManager().addListener(new ChatManagerListener() {

	    public void onChatClosed(final Chat chat) {
		final ChatPanel panel = chats.remove(chat.getOtherURI());
		conversationsPanel.removeChat(panel);
	    }

	    public void onChatCreated(final Chat chat) {
		createChatPanel(chat.getOtherURI(), chat);
	    }

	});

	xmpp.getRoster().addListener(new RosterListener() {
	    public void onItemPresenceChanged(final RosterItem item) {
	    }

	    public void onRosterChanged(final Collection<RosterItem> items) {
		conversationsPanel.setRoster(items);
	    }
	});
    }
}
