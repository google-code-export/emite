package com.calclab.emite.client.xmpp.session;

import com.calclab.suco.client.scopes.context.ContextedScope;

public class SessionScope extends ContextedScope<Session> {
    public SessionScope() {
	super(Session.class);
    }
}
