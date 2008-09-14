package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

/**
 * Session event plumbing.
 */
public abstract class AbstractSession implements Session {

    protected final Signal<Session.State> onStateChanged;
    protected final Signal<Presence> onPresence;
    protected final Signal<Message> onMessage;
    protected final Signal<XmppURI> onLoggedOut;
    protected final Signal<XmppURI> onLoggedIn;
    protected final Signal<IQ> onIQ;

    public AbstractSession() {
	this.onStateChanged = new Signal<Session.State>("session:onStateChanged");
	this.onPresence = new Signal<Presence>("session:onPresence");
	this.onMessage = new Signal<Message>("session:onMessage");
	this.onLoggedIn = new Signal<XmppURI>("session:onLoggedIn");
	this.onLoggedOut = new Signal<XmppURI>("session:onLoggedOut");
	this.onIQ = new Signal<IQ>("session:onIQ");
    }

    public void onIQ(final Slot<IQ> slot) {
	onIQ.add(slot);
    }

    public void onLoggedIn(final Slot<XmppURI> slot) {
	onLoggedIn.add(slot);
    }

    public void onLoggedOut(final Slot<XmppURI> slot) {
	onLoggedOut.add(slot);
    }

    public void onMessage(final Slot<Message> listener) {
	onMessage.add(listener);
    }

    public void onPresence(final Slot<Presence> listener) {
	onPresence.add(listener);
    }

    public void onStateChanged(final Slot<Session.State> listener) {
	onStateChanged.add(listener);
    }

}
