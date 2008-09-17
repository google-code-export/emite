package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Listener;

/**
 * Session event plumbing.
 */
public abstract class AbstractSession implements Session {

    protected final Event<Session.State> onStateChanged;
    protected final Event<Presence> onPresence;
    protected final Event<Message> onMessage;
    protected final Event<IQ> onIQ;

    public AbstractSession() {
	this.onStateChanged = new Event<Session.State>("session:onStateChanged");
	this.onPresence = new Event<Presence>("session:onPresence");
	this.onMessage = new Event<Message>("session:onMessage");
	this.onIQ = new Event<IQ>("session:onIQ");
    }

    public void onIQ(final Listener<IQ> listener) {
	onIQ.add(listener);
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
