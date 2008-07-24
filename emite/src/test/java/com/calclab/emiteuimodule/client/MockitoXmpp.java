package com.calclab.emiteuimodule.client;

import static org.mockito.Mockito.mock;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.ISession;
import com.calclab.emite.client.xmpp.session.SessionImpl;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.suco.client.modules.ModuleBuilder;

public class MockitoXmpp extends Xmpp {

    private final ChatManager chat;
    private final PresenceManager presenceManager;
    private final Roster roster;
    private final RosterManager rosterManager;
    private final ISession sessionImpl;

    public MockitoXmpp() {
	super(new ModuleBuilder());
	chat = mock(ChatManager.class);
	presenceManager = mock(PresenceManager.class);
	roster = mock(Roster.class);
	rosterManager = mock(RosterManager.class);
	sessionImpl = mock(SessionImpl.class);
    }

    @Override
    public ChatManager getChatManager() {
	return chat;
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
    public ISession getSession() {
	return sessionImpl;
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
