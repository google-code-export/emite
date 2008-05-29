package com.calclab.emite.client.modular;

import java.util.ArrayList;
import java.util.List;

public class Context<C> {
    private final Class<?> contextType;

    public Context(final Class<?> contextType) {
	this.contextType = contextType;
    }

    @SuppressWarnings("unchecked")
    public List<C> getInstances(final Container container) {
	final List<Provider<?>> providers = getContextProviders(container);
	final ArrayList<C> instances = new ArrayList<C>();
	for (final Provider<?> provider : providers) {
	    instances.add((C) provider.get());
	}
	return instances;
    }

    public void register(final Container container, final Provider<?> provider) {
	getContextProviders(container).add(provider);
    }

    private List<Provider<?>> getContextProviders(final Container container) {
	final ContextRegistry registry = getRegistry(container);
	final List<Provider<?>> providers = registry.getProviders(contextType);
	return providers;
    }

    private ContextRegistry getRegistry(final Container container) {
	if (!container.hasProvider(ContextRegistry.class)) {
	    container.registerProvider(ContextRegistry.class, new Provider<ContextRegistry>() {
		final ContextRegistry registry = new ContextRegistry();

		public ContextRegistry get() {
		    return registry;
		}
	    });
	}
	return container.getInstance(ContextRegistry.class);
    }
}
