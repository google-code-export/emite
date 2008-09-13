package com.calclab.emite.core.client.xmpp.session;

import com.calclab.suco.client.scope.context.ContextedScope;

public class SessionScope extends ContextedScope<Session> {
    public SessionScope() {
	super(Session.class);
    }
}
