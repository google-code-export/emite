/**
 *
 */
package com.calclab.modular.client.scopes.context;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.modular.client.container.Provider;

class ContextedProvider<T, C> implements Provider<T> {
    public final Class<T> type;
    private C context;
    private final HashMap<C, T> instances;
    private final Provider<T> unscoped;

    public ContextedProvider(final Class<T> type, final Provider<T> unscoped) {
	this.type = type;
	this.unscoped = unscoped;
	this.instances = new HashMap<C, T>();
    }

    public T get() {
	if (context == null) {
	    throw new RuntimeException("trying to create an instance of type " + type + " in a not existent context");
	}
	T instance = instances.get(context);
	if (instance == null) {
	    instance = unscoped.get();
	    instances.put(context, instance);
	}
	return instance;
    }

    public void setContext(final C context) {
	Log.debug("Setting context in provider: " + type);
	this.context = context;
    }
}
