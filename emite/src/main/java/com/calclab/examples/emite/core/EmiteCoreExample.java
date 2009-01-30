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
package com.calclab.examples.emite.core;

import com.calclab.emite.core.client.bosh.BoshSettings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

/**
 * A example of how to use the Emite Core funciontallity. A GWT program that
 * send a (surpise!) hello world message and receives messages and presence
 * 
 */
public class EmiteCoreExample implements EntryPoint {

    public void onModuleLoad() {
	/*
	 * First, we have to configure the server settings. Normally this is
	 * done in the html (not in the code)... but this is only an example
	 */
	Suco.get(Connection.class).setSettings(new BoshSettings("/myProxyURL", "myServerHostName"));

	/*
	 * We get the Session object. The most important object of Emite Core
	 * module.
	 */
	final Session session = Suco.get(Session.class);

	/*
	 * We track session state changes. We can only send messages when the
	 * state == loggedIn.
	 */
	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final State state) {
		if (state == Session.State.loggedIn) {
		    GWT.log("We are now online", null);
		    sendHelloWorldMessage(session);
		} else if (state == Session.State.disconnected) {
		    GWT.log("We are now offline", null);
		}
	    }
	});

	/*
	 * We show every incoming message in the GWT log console
	 */
	session.onMessage(new Listener<Message>() {
	    public void onEvent(final Message message) {
		GWT.log("Messaged received from " + message.getFrom() + ":" + message.getBody(), null);
	    }
	});

	/*
	 * We show (log) every incoming presence stanzas
	 */
	session.onPresence(new Listener<Presence>() {
	    public void onEvent(final Presence presence) {
		GWT.log("Presence received from " + presence.getFrom() + ": " + presence.toString(), null);
	    }
	});

	session.login(uri("myJID@myDomain.org"), "myPassword");
    }

    /**
     * The simplest way to send a message using the Session object
     */
    private void sendHelloWorldMessage(final Session session) {
	final Message message = new Message("hello world!", uri("everybody@world.org"));
	session.send(message);
    }
}
