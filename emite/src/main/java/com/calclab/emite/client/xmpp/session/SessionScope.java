package com.calclab.emite.client.xmpp.session;

import com.calclab.suco.client.scopes.context.ContextedScope;

public class SessionScope extends ContextedScope<ISession> {
    public SessionScope() {
	super(ISession.class);
    }
}
