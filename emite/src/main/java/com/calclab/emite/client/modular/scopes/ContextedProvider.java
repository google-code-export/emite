/**
 *
 */
package com.calclab.emite.client.modular.scopes;

import java.util.HashMap;

import com.calclab.emite.client.modular.Provider;

class ContextedProvider<T, C> implements Provider<T> {
    private C context;
    private final HashMap<C, T> instances;
    private final Provider<T> unscoped;
    private final Class<T> type;

    public ContextedProvider(final Class<T> type, final Provider<T> unscoped) {
	this.type = type;
	this.unscoped = unscoped;
	this.instances = new HashMap<C, T>();
    }

    public T get() {
	if (context == null) {
	    throw new RuntimeException("trying to create an instance of type " + type + "in a not existent context");
	}
	T instance = instances.get(context);
	if (instance == null) {
	    instance = unscoped.get();
	    instances.put(context, instance);
	}
	return instance;
    }

    public void setContext(final C context) {
	this.context = context;
    }
}
