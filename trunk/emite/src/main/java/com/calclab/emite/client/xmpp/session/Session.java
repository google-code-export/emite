package com.calclab.emite.client.xmpp.session;

import com.calclab.emite.client.core.bosh.StreamSettings;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Slot;

/**
 * The most important object in Xmpp emite module. You can login, send and
 * receive stanzas. It also allows you to pause and resume the session.
 * 
 */
public interface Session {

    /**
     * Different session states
     */
    public static enum State {
	authorized, loggedIn, connecting, disconnected, error, notAuthorized, ready
    }

    /**
     * Returns the current user xmpp uri
     * 
     * @return the current user xmpp uri
     */
    public abstract XmppURI getCurrentUser();

    /**
     * Returns the current state
     * 
     * @return a session.state enum type
     */
    public abstract State getState();

    /**
     * Answer if is logged in or not
     * 
     * @return true if a user is logged in
     */
    public abstract boolean isLoggedIn();

    /**
     * Start a loggin process with the current xmpp uri and password. Use
     * onLoggedIn method to know when you are really logged in.
     * 
     * @param uri
     *            the user's uri to loggin
     * @param password
     *            the user's password
     */
    public abstract void login(final XmppURI uri, final String password);

    /**
     * Start a logout process in the current session. Use obnLoggedOut to know
     * when you are really logged out.
     */
    public abstract void logout();

    /**
     * The given slot is called when the user has logged in into the session
     * 
     * @param slot
     *            callback
     */
    public abstract void onLoggedIn(final Slot<XmppURI> slot);

    /**
     * The given slot is called when the user has logged out into the session
     * 
     * @param slot
     */
    public abstract void onLoggedOut(final Slot<XmppURI> slot);

    /**
     * The given slot is called when a message stanza has arrived
     * 
     * @param slot
     * 
     */
    public abstract void onMessage(final Slot<Message> slot);

    /**
     * The given slot is called when a presence stanza has arrived
     * 
     * @param slot
     */
    public abstract void onPresence(final Slot<Presence> slot);

    /**
     * The given slot is called when the session changed it's state
     * 
     * @param slot
     */
    public abstract void onStateChanged(final Slot<State> slot);

    /**
     * Call this method to pause the session. You can use the given object
     * object (or other with the same data) to resume the session later.
     * 
     * @see http://www.xmpp.org/extensions/xep-0124.html#inactive
     * @see Session.resume
     * @return
     */
    public abstract StreamSettings pause();

    /**
     * Call this method to resume a session.
     * 
     * @see http://www.xmpp.org/extensions/xep-0124.html#inactive
     * @see Session.pause
     * @param userURI
     *            the previous session user's uri
     * @param settings
     *            the stream settings given by the pause method
     */
    public abstract void resume(XmppURI userURI, StreamSettings settings);

    /**
     * Send a stanza to the server.
     * 
     * @see sendIQ
     * @param packet
     *            the packet to be sent
     */
    public abstract void send(final IPacket packet);

    /**
     * A helper method that allows to send a IQ stanza and attach a callback to
     * the response. This method overrides the given IQ id using the category
     * provided and a internal sequential number.
     * 
     * @param category
     *            a uniqe-per-component string that allows the session to
     *            generate a sequential and uniqe id for the IQ
     * @param iq
     *            the IQ stanza to be sent
     * @param callback
     *            the callback called when a IQ of type "result" arrives to the
     *            server. After the invocation, the callback is discarded
     * 
     */
    public abstract void sendIQ(final String category, final IQ iq, final Slot<IPacket> callback);

}
