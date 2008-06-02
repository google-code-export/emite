package com.calclab.emite.client.modular.scopes;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emite.client.xmpp.session.Session;

/**
 * The idea: singleton for each context NOT FULLY IMPLEMENTED: currently are
 * delayed created instances (even not singletons!) -> enough for SessionScope
 * purposes
 * 
 */
public class Context implements Scope {
    public static class ContextProvider<C> implements Provider<C> {

	private final Class<C> type;
	private final Provider<C> unscoped;

	public ContextProvider(final Class<C> type, final Provider<C> unscoped) {
	    this.type = type;
	    this.unscoped = unscoped;
	}

	public C get() {
	    Log.debug("New context instance: " + type);
	    return unscoped.get();
	}

    }

    private final ArrayList<Provider<?>> providers;

    public Context() {
	this.providers = new ArrayList<Provider<?>>();
    }

    // FIXME: This only works if session is an instance
    // TODO: create a new container to fix that
    public void newContext(final Session session) {
	for (final Provider<?> provider : providers) {
	    provider.get();
	}
    }

    public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	final Provider<T> provider = new ContextProvider<T>(type, unscoped) {

	};
	providers.add(provider);
	return provider;
    }

}
