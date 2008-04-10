package com.calclab.examplechat.client;

import static org.mockito.Mockito.mock;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.DefaultContainer;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherMonitor;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;

public class MockitoXmpp extends Xmpp {

    private final ChatManager chat;
    private final Container components;
    private final Dispatcher dispatcher;
    private final PresenceManager presenceManager;
    private final Roster roster;
    private final RosterManager rosterManager;
    private final Session session;

    public MockitoXmpp() {
	super(new DefaultContainer(), null, new DispatcherMonitor() {
	    public void publishing(final IPacket packet) {
	    }
	});
	chat = mock(ChatManager.class);
	components = mock(Container.class);
	dispatcher = mock(Dispatcher.class);
	presenceManager = mock(PresenceManager.class);
	roster = mock(Roster.class);
	rosterManager = mock(RosterManager.class);
	session = mock(Session.class);
    }

    @Override
    public ChatManager getChatManager() {
	return chat;
    }

    @Override
    public Container getComponents() {
	return components;
    }

    @Override
    public Dispatcher getDispatcher() {
	return dispatcher;
    }

    @Override
    public PresenceManager getPresenceManager() {
	return presenceManager;
    }

    @Override
    public Roster getRoster() {
	return roster;
    }

    @Override
    public RosterManager getRosterManager() {
	return rosterManager;
    }

    @Override
    public Session getSession() {
	return session;
    }

    @Override
    public void login(final XmppURI uri, final String password, final Show show, final String status) {
	throw new RuntimeException("not implemented");
    }

    @Override
    public void logout() {
	throw new RuntimeException("not implemented");
    }

}
