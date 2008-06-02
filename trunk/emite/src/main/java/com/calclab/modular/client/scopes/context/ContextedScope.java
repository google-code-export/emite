package com.calclab.modular.client.scopes.context;

import java.util.ArrayList;
import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.modular.client.container.Container;
import com.calclab.modular.client.container.Provider;
import com.calclab.modular.client.scopes.Scope;

/**
 * A contexted scope is a kind of generic scope controlled by context: a
 * singleton instance for each context. A Context is just a POJO (that
 * implements equals contract)
 * 
 * @param <O>
 *                The context type
 */
public class ContextedScope<O> implements Scope, Context<O> {
    public HashMap<Class<?>, Container> contexts;
    private final ArrayList<ContextedProvider<?, O>> providers;

    private O currentContext;
    private final Class<O> contextType;

    public ContextedScope(final Class<O> contextType) {
	Log.debug("Contexted scope for: " + contextType);
	this.contextType = contextType;
	currentContext = null;
	this.providers = new ArrayList<ContextedProvider<?, O>>();
    }

    public void createAll() {
	Log.debug("CREATE ALL in context " + contextType);
	for (final ContextedProvider<?, O> provider : providers) {
	    if (!provider.isCreated()) {
		Log.debug("CREATED BY CONTEXT: " + provider.type);
		provider.get();
	    }
	}
    }

    public O getContext() {
	return currentContext;
    }

    public void removeContext(final O contextObject) {

    }

    public <T> Provider<T> scope(final Class<T> type, final Provider<T> unscoped) {
	final ContextedProvider<T, O> provider = new ContextedProvider<T, O>(type, unscoped);
	Log.debug("Contexted provider: " + type);
	providers.add(provider);
	if (currentContext != null) {
	    provider.setContext(currentContext);
	}
	return provider;
    }

    public void setContext(final O context) {
	Log.debug("New context: " + context);
	currentContext = context;
	for (final ContextedProvider<?, O> provider : providers) {
	    provider.setContext(context);
	}
    }

    @Override
    public String toString() {
	final String repr = currentContext != null ? currentContext.toString() : "none";
	return "[" + contextType + " context. Current: " + repr + "]";
    }
}
