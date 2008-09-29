package com.calclab.emite.widgets.habla.client;

import java.util.Map;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.ChatImpl;
import com.calclab.suco.client.listener.Listener;

public class HablaController {
    private final Session session;
    private final HablaWidget widget;
    private Conversation conversation;

    public HablaController(final Session session, final HablaWidget widget) {
	this.session = session;
	this.widget = widget;
	widget.setEnabled(false);

	widget.onSetProperties(new Listener<Map<String, String>>() {
	    public void onEvent(final Map<String, String> properties) {
		final XmppURI jid = XmppURI.uri(properties.get("jid"));
		if (jid == null) {
		    widget.showStatus("JID not specified or not valid.", "error");
		    throw new RuntimeException("JID property not specified or not valid.");
		}
		openChat(jid);
	    }
	});

	widget.onMessage(new Listener<String>() {
	    public void onEvent(final String messageBody) {
		widget.show("me", messageBody);
		conversation.send(new Message(messageBody));
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State state) {
		widget.showStatus(state.toString(), "");
		switch (state) {
		case ready:
		    break;
		}
	    }
	});
    }

    private void openChat(final XmppURI jid) {
	final String name = jid.getNode();
	conversation = new ChatImpl(session, jid, null);
	conversation.onStateChanged(new Listener<Conversation.State>() {
	    public void onEvent(final Conversation.State state) {
		widget.setEnabled(state == Conversation.State.ready);
	    }
	});

	conversation.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		widget.show(name, message.getBody());
	    }
	});
    }

}
