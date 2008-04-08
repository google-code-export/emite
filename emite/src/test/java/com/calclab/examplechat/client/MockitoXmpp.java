package com.calclab.examplechat.client;

import static org.mockito.Mockito.mock;

import com.calclab.emite.client.AbstractXmpp;
import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.Session;

public class MockitoXmpp implements AbstractXmpp {

    private final ChatManager chat;
    private final Container components;
    private final Dispatcher dispatcher;
    private final PresenceManager presenceManager;
    private final Roster roster;
    private final RosterManager rosterManager;
    private final Session session;

    public MockitoXmpp() {
	chat = mock(ChatManager.class);
	components = mock(Container.class);
	dispatcher = mock(Dispatcher.class);
	presenceManager = mock(PresenceManager.class);
	roster = mock(Roster.class);
	rosterManager = mock(RosterManager.class);
	session = mock(Session.class);
    }

    public ChatManager getChatManager() {
	return chat;
    }

    public Container getComponents() {
	return components;
    }

    public Dispatcher getDispatcher() {
	return dispatcher;
    }

    public PresenceManager getPresenceManager() {
	return presenceManager;
    }

    public Roster getRoster() {
	return roster;
    }

    public RosterManager getRosterManager() {
	return rosterManager;
    }

    public Session getSession() {
	return session;
    }

    public void login(final String userName, final String userPassword) {
	throw new RuntimeException("not implemented");
    }

    public void logout() {
	throw new RuntimeException("not implemented");
    }

}
