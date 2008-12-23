package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.suco.client.events.Listener;

public abstract class AbstractChatController {

    protected AbstractChatWidget widget;
    protected Conversation conversation;
    protected final Session session;
    protected final ChatManager manager;

    public AbstractChatController(final Session session, final ChatManager manager) {
	this.session = session;
	this.manager = manager;
    }

    public void setChat(final Conversation conversation) {
	this.conversation = conversation;
	conversation.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		widget.write(getFromUserName(message), message.getBody());
	    }
	});

	conversation.onMessageSent(new Listener<Message>() {
	    public void onEvent(final Message message) {
		widget.write("me", message.getBody());
	    }
	});
    }

    protected abstract XmppURI getChatURI();

    protected abstract String getFromUserName(final Message message);

    protected void init() {
	widget.setInputEnabled(false);
	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final State state) {
		if (state == State.ready) {
		    openChat();
		} else {
		    widget.setInputEnabled(false);
		}
	    }
	});

	manager.onChatCreated(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		if (isOurChat(conversation)) {
		    widget.write(null, "chat ready.");
		    setChat(conversation);
		    widget.setInputEnabled(true);
		}
	    }
	});

	widget.onSendMessage(new Listener<String>() {
	    public void onEvent(final String body) {
		conversation.send(new Message(body));
	    }
	});
    }

    protected abstract boolean isOurChat(Conversation conversation);

    protected void setWidget(final AbstractChatWidget widget) {
	this.widget = widget;
	init();
    }

    private void openChat() {
	// if other chatWidget open the same chat before this widget do, then
	// the listener onChatCreated is called before manager.openChat
	final XmppURI chatURI = getChatURI();
	if (chatURI != null) {
	    if (conversation == null) {
		widget.write(null, "opening chat...");
		manager.openChat(chatURI, null, null);
	    } else {
		widget.setInputEnabled(true);
	    }
	}
    }

}
