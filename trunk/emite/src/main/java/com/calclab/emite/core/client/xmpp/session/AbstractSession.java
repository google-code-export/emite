package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Listener;

/**
 * Session event plumbing.
 */
public abstract class AbstractSession implements Session {

    protected final Event<Session.State> onStateChanged;
    protected final Event<Presence> onPresence;
    protected final Event<Message> onMessage;
    protected final Event<XmppURI> onLoggedOut;
    protected final Event<XmppURI> onLoggedIn;
    protected final Event<IQ> onIQ;

    public AbstractSession() {
	this.onStateChanged = new Event<Session.State>("session:onStateChanged");
	this.onPresence = new Event<Presence>("session:onPresence");
	this.onMessage = new Event<Message>("session:onMessage");
	this.onLoggedIn = new Event<XmppURI>("session:onLoggedIn");
	this.onLoggedOut = new Event<XmppURI>("session:onLoggedOut");
	this.onIQ = new Event<IQ>("session:onIQ");
    }

    public void onIQ(final Listener<IQ> slot) {
	onIQ.add(slot);
    }

    public void onLoggedIn(final Listener<XmppURI> slot) {
	onLoggedIn.add(slot);
    }

    public void onLoggedOut(final Listener<XmppURI> slot) {
	onLoggedOut.add(slot);
    }

    public void onMessage(final Listener<Message> listener) {
	onMessage.add(listener);
    }

    public void onPresence(final Listener<Presence> listener) {
	onPresence.add(listener);
    }

    public void onStateChanged(final Listener<Session.State> listener) {
	onStateChanged.add(listener);
    }

}
