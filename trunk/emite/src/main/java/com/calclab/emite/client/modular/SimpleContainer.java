package com.calclab.emite.client.modular;

import java.util.HashMap;

/**
 * A class that implements the Container interface using a HashMap
 * 
 * @author dani
 */
@SuppressWarnings("serial")
public class SimpleContainer extends HashMap<Class<?>, Provider<?>> implements Container {

    public <T> T getInstance(final Class<T> componentKey) {
	return getProvider(componentKey).get();
    }

    public <T> Provider<T> getProvider(final Class<T> componentKey) {
	final Provider<T> provider = (Provider<T>) super.get(componentKey);
	if (provider == null) {
	    throw new RuntimeException("component not registered: " + componentKey);
	}
	return provider;
    }

    public <T> T register(final Class<T> componentType, final T component) {
	registerProvider(componentType, new Provider<T>() {
	    public T get() {
		return component;
	    }
	});
	return component;
    }

    public <T> Provider<T> registerProvider(final Class<T> componentKey, final Provider<T> provider) {
	super.put(componentKey, provider);
	return provider;
    }
}
