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
package com.calclab.emite.im.client.chat;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Message.Type;
import com.calclab.suco.client.listener.Listener;

/**
 * <p>
 * Default Chat implementation. Use Chat interface instead
 * </p>
 * 
 * <p>
 * About Chat ids: Other sender Uri plus thread identifies a chat (associated
 * with a chat panel in the UI). If no thread is specified, we join all messages
 * in one chat.
 * </p>
 * 
 * @see Conversation
 */
public class ChatImpl extends AbstractChat {
    protected final String thread;
    private final String id;

    public ChatImpl(final Session session, final XmppURI other, final String thread) {
	super(session, other);
	this.thread = thread;
	this.id = generateChatID();
	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State state) {
		setState(getStateFromSessionState(state));
	    }

	});
	setState(getStateFromSessionState(session.getState()));
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj == null) {
	    return false;
	}
	if (this == obj) {
	    return true;
	}
	final ChatImpl other = (ChatImpl) obj;
	return id.equals(other.id);
    }

    public String getID() {
	return id;
    }

    public String getThread() {
	return thread;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (other == null ? 0 : other.hashCode());
	result = prime * result + (thread == null ? 0 : thread.hashCode());
	return result;
    }

    @Override
    public void send(final Message message) {
	message.setThread(thread);
	message.setType(Type.chat);
	message.setTo(other);
	super.send(message);
    }

    @Override
    public String toString() {
	return id;
    }

    private String generateChatID() {
	return "chat: " + other.toString() + "-" + thread;
    }

    private State getStateFromSessionState(final Session.State state) {
	switch (state) {
	case loggedIn:
	case ready:
	    return State.ready;
	default:
	    return State.locked;
	}
    }

}
