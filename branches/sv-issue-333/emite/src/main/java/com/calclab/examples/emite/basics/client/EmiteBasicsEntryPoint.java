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
package com.calclab.examples.emite.basics.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.Collection;

import com.calclab.emite.core.client.bosh.BoshSettings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

public class EmiteBasicsEntryPoint implements EntryPoint {

    private TextArea area;

    public void onModuleLoad() {
	initOutput();

	// Suco is a facade that give access to every emite component we need

	// configureConnection(); // we use Browser emite module

	// ******** 1. Session *********
	// Session is the emite component that allows us to login/logout among
	// other things
	final Session session = Suco.get(Session.class);

	// Session.onStateChanged allows us to know the state of the session
	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State state) {
		print("Session state: " + state);
	    }
	});

	// Session.login and Session.logout are our xmpp entrance and exit
	session.login(uri("test1@localhost"), "test1");

	// After login, we can send messages ...
	session.send(new Message("Hello", uri("test2@localhost")));
	// ... or receive messages ...
	session.onMessage(new Listener<Message>() {
	    public void onEvent(final Message message) {
		print("Message arrived: " + message.getBody());
	    }
	});

	// ******** 2. ChatManager *********
	// ... but probably you prefer to use the a powerful abstraction: Chat
	final ChatManager chatManager = Suco.get(ChatManager.class);
	final Chat chat = chatManager.open(uri("test2@localhost"));
	// with chats you don't have to specify the recipient
	chat.send(new Message("Hello test2"));
	// and you only receive messages from the entity you specified
	chat.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		print("Message from test2 arrived: " + message.getBody());
	    }
	});

	// ******** 3. Roster *********
	// As always, Suco is our friend...
	final Roster roster = Suco.get(Roster.class);
	// ... we're in asynchronous world... use listeners
	// onRosterRetrieved is fired when... surprise! the roster is retrieved
	roster.onRosterRetrieved(new Listener<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> items) {
		print("We have the roster");
		for (final RosterItem item : items) {
		    print("Roster item: " + item);
		}
	    }
	});
	// we can track changes in roster items (i.e. roster presence changes)
	// using Roster.onItemUpdated
	roster.onItemChanged(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		print("Roster item changed:" + item);
	    }
	});
    }

    protected void configureConnection() {
	// ******** 0. Configure connection settings *********
	final Connection connection = Suco.get(Connection.class);
	connection.setSettings(new BoshSettings("proxy", "localhost"));
	// ...but there's a module, BrowserModule, that allows to configure
	// the connections settings in the html directly
    }

    private void initOutput() {
	area = new TextArea();
	RootPanel.get().add(area);
	print("Welcome to emite basics example.");
    }

    /**
     * a helper method to output messages
     * 
     * @param message
     */
    private void print(final String message) {
	area.setText(area.getText() + "\n" + message);
    }

}
