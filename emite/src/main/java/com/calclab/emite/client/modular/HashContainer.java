package com.calclab.emite.client.modular;

import java.util.HashMap;

/**
 * A class that implements the Container interface using a HashMap
 * 
 * @author dani
 */
public class HashContainer implements Container {
    private final HashMap<Class<?>, Provider<?>> components;

    public HashContainer() {
	this.components = new HashMap<Class<?>, Provider<?>>();
    }

    public <T> T getInstance(final Class<T> componentKey) {
	return getProvider(componentKey).get();
    }

    @SuppressWarnings("unchecked")
    public <T> Provider<T> getProvider(final Class<T> componentKey) {
	final Provider<T> provider = (Provider<T>) components.get(componentKey);
	if (provider == null) {
	    throw new RuntimeException("component not registered: " + componentKey);
	}
	return provider;
    }

    public <T> T registerSingletonInstance(final Class<T> componentType, final T component) {
	registerProvider(componentType, new Provider<T>() {
	    public T get() {
		return component;
	    }
	});
	return component;
    }

    public <T> Provider<T> registerProvider(final Class<T> componentKey, final Provider<T> provider) {
	components.put(componentKey, provider);
	return provider;
    }
}
