package com.calclab.emite.client.xmpp.session;

import com.calclab.emite.client.core.bosh3.Bosh3Settings;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Slot;

public interface Session {

    public static enum State {
	authorized, loggedIn, connecting, disconnected, error, notAuthorized, ready
    }

    public abstract XmppURI getCurrentUser();

    public abstract Session.State getState();

    public abstract boolean isLoggedIn();

    public abstract void login(final XmppURI uri, final String password, final Bosh3Settings settings);

    public abstract void logout();

    public void onIQ(final Slot<IQ> slot);

    public abstract void onLoggedIn(final Slot<XmppURI> slot);

    public abstract void onLoggedOut(final Slot<XmppURI> slot);

    public abstract void onMessage(final Slot<Message> listener);

    public abstract void onPresence(final Slot<Presence> listener);

    public abstract void onStateChanged(final Slot<Session.State> listener);

    public abstract void send(final IPacket packet);

    public abstract void sendIQ(final String id, final IQ iq, final Slot<IPacket> slot);

    public abstract void setLoggedIn(final XmppURI userURI);

}
