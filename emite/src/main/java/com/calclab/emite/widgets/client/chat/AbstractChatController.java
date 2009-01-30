package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.suco.client.events.Listener;

public abstract class AbstractChatController {

    protected AbstractChatWidget widget;
    protected Chat chat;
    protected final Session session;
    protected final ChatManager manager;

    public AbstractChatController(final Session session, final ChatManager manager) {
	this.session = session;
	this.manager = manager;
    }

    public void setChat(final Chat chat) {
	this.chat = chat;
	chat.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		widget.write(getFromUserName(message), message.getBody());
	    }
	});

	chat.onMessageSent(new Listener<Message>() {
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

	manager.onChatCreated(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		if (isOurChat(chat)) {
		    widget.write(null, "chat ready.");
		    setChat(chat);
		    widget.setInputEnabled(true);
		}
	    }
	});

	widget.onSendMessage(new Listener<String>() {
	    public void onEvent(final String body) {
		chat.send(new Message(body));
	    }
	});
    }

    protected abstract boolean isOurChat(Chat chat);

    protected void setWidget(final AbstractChatWidget widget) {
	this.widget = widget;
	init();
    }

    private void openChat() {
	// if other chatWidget open the same chat before this widget do, then
	// the listener onChatCreated is called before manager.openChat
	final XmppURI chatURI = getChatURI();
	if (chatURI != null) {
	    if (chat == null) {
		widget.write(null, "opening chat...");
		manager.open(chatURI);
	    } else {
		widget.setInputEnabled(true);
	    }
	}
    }

}
