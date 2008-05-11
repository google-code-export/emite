package com.calclab.emite.client.modular;

import java.util.HashMap;

@SuppressWarnings("serial")
public class SimpleContainer extends HashMap<Class<?>, Object> implements Container {
    public <T> T get(final Class<T> componentType) {
	final T component = (T) super.get(componentType);
	if (component == null) {
	    throw new RuntimeException("component not registered: " + componentType);
	}
	return component;
    }

    public <T> T register(final Class<T> componentType, final T component) {
	super.put(componentType, component);
	return component;
    }
}
