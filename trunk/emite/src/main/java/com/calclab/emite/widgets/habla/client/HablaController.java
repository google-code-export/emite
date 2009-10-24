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
package com.calclab.emite.widgets.habla.client;

import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.suco.client.events.Listener;

public class HablaController {
    private final HablaWidget widget;
    private Chat chat;

    public HablaController(final Session session, final ChatManager chatManager, final HablaWidget widget) {
	Log.debug("Creating Habla controller");
	this.widget = widget;
	widget.setEnabled(false);

	widget.onSetProperties(new Listener<Map<String, String>>() {
	    public void onEvent(final Map<String, String> properties) {
		final XmppURI jid = XmppURI.uri(properties.get("jid"));
		if (jid == null) {
		    widget.showStatus("JID not specified or not valid.", "error");
		    throw new RuntimeException("JID property not specified or not valid.");
		}
		openChat(chatManager, jid);
	    }
	});

	widget.onMessage(new Listener<String>() {
	    public void onEvent(final String messageBody) {
		widget.show("me", messageBody);
		chat.send(new Message(messageBody));
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State state) {
		setState(state);
	    }
	});
	// AutoConfig starts before I do!!
	setState(session.getState());
    }

    private void openChat(final ChatManager chatManager, final XmppURI jid) {
	final String name = jid.getNode();
	chat = chatManager.open(jid);
	chat.onStateChanged(new Listener<Chat.State>() {
	    public void onEvent(final Chat.State state) {
		widget.setEnabled(state == Chat.State.ready);
	    }
	});

	chat.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		widget.show(name, message.getBody());
	    }
	});
    }

    private void setState(final Session.State state) {
	Log.debug("Hablar state: " + state);
	widget.showStatus(state.toString(), "");
	switch (state) {
	case ready:
	    break;
	}
	widget.setEnabled(state == State.ready);
    }

}
