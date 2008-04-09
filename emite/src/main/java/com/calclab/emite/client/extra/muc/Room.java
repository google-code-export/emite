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
package com.calclab.emite.client.extra.muc;

import java.util.ArrayList;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class Room implements Chat {
    private final XmppURI roomURI;
    private final String name;
    private final ArrayList<RoomUser> users;
    private final ArrayList<RoomListener> listeners;
    private final Emite emite;
    private final XmppURI userURI;

    public Room(final XmppURI userURI, final XmppURI roomURI, final String name, final Emite emite) {
	this.userURI = userURI;
	this.roomURI = roomURI;
	this.name = name;
	this.emite = emite;
	this.users = new ArrayList<RoomUser>();
	this.listeners = new ArrayList<RoomListener>();
    }

    public void addListener(final ChatListener listener) {

    }

    public void addListener(final RoomListener listener) {
	listeners.add(listener);
    }

    public String getID() {
	return roomURI.toString();
    }

    public String getName() {
	return name;
    }

    public XmppURI getOtherURI() {
	return roomURI;
    }

    public String getThread() {
	return roomURI.getNode();
    }

    public XmppURI getURI() {
	return roomURI;
    }

    public void send(final String text) {
	final Message message = new Message(userURI, roomURI, text);
	message.setType(Message.Type.groupchat);
	emite.send(message);
	for (final RoomListener listener : listeners) {
	    listener.onMessageSent(this, message);
	}
    }

    void addUser(final RoomUser roomUser) {
	users.add(roomUser);
	fireUserChanged();
    }

    void dispatch(final Message message) {

    }

    private void fireUserChanged() {
	for (final RoomListener listener : listeners) {
	    listener.onUserChanged(users);
	}
    }

}
