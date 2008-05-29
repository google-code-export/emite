package com.calclab.emite.client.xmpp.sasl;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class AuthorizationTicket {
    public static enum State {
	succeed, failed, notStarted, waitingForAuthorization
    }

    public final XmppURI uri;
    private String password;
    private State state;

    public AuthorizationTicket(final XmppURI uri, final String password) {
	this.uri = uri;
	this.password = password;
	this.state = State.notStarted;
    }

    /**
     * Testing purposes only! Not state logic!
     */
    public AuthorizationTicket(final XmppURI uri, final String password, final State state) {
	this(uri, password);
	this.state = state;
    }

    public String getPassword() {
	return password;
    }

    public State getState() {
	return state;
    }

    public void setState(final State state) {
	this.state = state;
	this.password = null;
    }

}
