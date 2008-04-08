package com.calclab.emite.client;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.Session;

public interface AbstractXmpp {

    public ChatManager getChatManager();

    public Container getComponents();

    public Dispatcher getDispatcher();

    public PresenceManager getPresenceManager();

    public Roster getRoster();

    public RosterManager getRosterManager();

    public Session getSession();

    public void login(final String userName, final String userPassword);

    public void logout();

}