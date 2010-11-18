/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
