package com.calclab.emite.client.modular;

import java.util.HashMap;
import java.util.List;

public class ContextRegistry {
    private final HashMap<Class<?>, List<Provider<?>>> registry;

    public ContextRegistry() {
	this.registry = new HashMap<Class<?>, List<Provider<?>>>();
    }

    public List<Provider<?>> getProviders(final Class<?> contextType) {
	return registry.get(contextType);
    }

}
