package com.calclab.emite.client.modular.scopes;

import java.util.ArrayList;
import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Provider;

public class ContextedScope<O> implements Scope {
    public HashMap<Class<?>, Container> contexts;
    private final ArrayList<ContextedProvider<?, O>> providers;

    private O currentContext;

    public ContextedScope() {
	currentContext = null;
	this.providers = new ArrayList<ContextedProvider<?, O>>();
    }

    public void createAll() {
	for (final ContextedProvider<?, O> provider : providers) {
	    provider.get();
	}
    }

    public O getContext() {
	return currentContext;
    }

    public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	final ContextedProvider<T, O> provider = new ContextedProvider<T, O>(type, unscoped);
	providers.add(provider);
	return provider;
    }

    public void setContext(final O context) {
	Log.debug("New context: " + context);
	currentContext = context;
	for (final ContextedProvider<?, O> provider : providers) {
	    provider.setContext(context);
	}
    }
}
