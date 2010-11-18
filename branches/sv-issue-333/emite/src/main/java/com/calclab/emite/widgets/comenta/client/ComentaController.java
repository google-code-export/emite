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
package com.calclab.emite.widgets.comenta.client;

import java.util.Map;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.events.Listener;

public class ComentaController {
    private static final String CONSONANTES = "bcdfghjklmnpqrstwxyz";
    private static final String VOCALES = "aeiuo";
    private final ComentaWidget widget;
    private Chat room;

    public ComentaController(final Session session, final RoomManager roomManager, final ComentaWidget widget) {
	this.widget = widget;
	widget.setEnabled(false);

	widget.onSetProperties(new Listener<Map<String, String>>() {
	    public void onEvent(final Map<String, String> properties) {
		final XmppURI roomJID = XmppURI.uri(properties.get("room"));
		if (roomJID == null) {
		    widget.showStatus("Room not specified or not valid.", "error");
		    throw new RuntimeException("room property not specified or not valid.");
		}
		final XmppURI roomURI = XmppURI.uri(roomJID.getNode(), roomJID.getHost(), generateNick());
		createRoom(roomManager, roomURI);
	    }
	});

	widget.onMessage(new Listener<String>() {
	    public void onEvent(final String messageBody) {
		room.send(new Message(messageBody));
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State state) {
		widget.showStatus(state.toString(), "");
		switch (state) {
		case ready:
		    widget.showStatus("Entering " + room.getURI().getNode() + "...", "info");
		    break;
		}
	    }
	});

    }

    private void createRoom(final RoomManager roomManager, final XmppURI roomURI) {
	room = roomManager.open(roomURI);

	room.onStateChanged(new Listener<Chat.State>() {
	    public void onEvent(final State state) {
		final boolean isReady = state == State.ready;
		if (isReady) {
		    widget.setEnabled(true);
		    final XmppURI uri = room.getURI();
		    widget.showStatus("You are " + uri.getResource() + " in " + uri.getNode(), "ready");
		} else {
		    widget.setEnabled(false);
		    widget.showStatus("waiting for room...", "info");
		}
	    }
	});

	room.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		widget.show(message.getFrom().getResource(), message.getBody());
	    }
	});

    }

    private String generateNick() {
	return select(CONSONANTES) + select(VOCALES) + select(VOCALES) + select(CONSONANTES) + select(VOCALES);
    }

    private String select(final String posibilities) {
	final int pos = (int) (Math.random() * posibilities.length());
	return posibilities.substring(pos, pos + 1);
    }
}
