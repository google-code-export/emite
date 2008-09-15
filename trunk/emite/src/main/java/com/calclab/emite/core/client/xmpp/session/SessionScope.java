package com.calclab.emite.core.client.xmpp.session;

import com.calclab.suco.client.provider.Provider;
import com.calclab.suco.client.scope.context.ContextedScope;

public class SessionScope extends ContextedScope<Session> {
    private static final Provider<SessionScope> provider = new Provider<SessionScope>() {
	private final SessionScope instance = new SessionScope();

	public SessionScope get() {
	    return instance;
	}
    };

    public static Provider<SessionScope> getProvider() {
	return provider;
    }

    public SessionScope() {
	super(Session.class);
    }
}
