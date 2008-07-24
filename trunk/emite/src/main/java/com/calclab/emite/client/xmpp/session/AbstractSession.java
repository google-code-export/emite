package com.calclab.emite.client.xmpp.session;

import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public abstract class AbstractSession implements ISession {

    protected final Signal<ISession.State> onStateChanged;
    protected final Signal<Presence> onPresence;
    protected final Signal<Message> onMessage;
    protected final Signal<IQ> onIQ;
    protected final Signal<ISession> onLoggedOut;
    protected final Signal<XmppURI> onLoggedIn;

    public AbstractSession() {
	this.onStateChanged = new Signal<ISession.State>("session:onStateChanged");
	this.onPresence = new Signal<Presence>("session:onPresence");
	this.onMessage = new Signal<Message>("session:onMessage");
	this.onIQ = new Signal<IQ>("session:onIQ");
	this.onLoggedIn = new Signal<XmppURI>("session:onLoggedIn");
	this.onLoggedOut = new Signal<ISession>("session:onLoggedOut");
    }

    public void onIQ(final Slot<IQ> slot) {
	onIQ.add(slot);
    }

    public void onLoggedIn(final Slot<XmppURI> slot) {
	onLoggedIn.add(slot);
    }

    public void onLoggedOut(final Slot<ISession> slot) {
	onLoggedOut.add(slot);
    }

    public void onMessage(final Slot<Message> listener) {
	onMessage.add(listener);
    }

    public void onPresence(final Slot<Presence> listener) {
	onPresence.add(listener);
    }

    public void onStateChanged(final Slot<ISession.State> listener) {
	onStateChanged.add(listener);
    }

}
