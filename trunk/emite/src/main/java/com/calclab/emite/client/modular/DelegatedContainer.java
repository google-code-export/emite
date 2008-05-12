package com.calclab.emite.client.modular;

/**
 * Implements a container ussing the Delegate pattern
 * 
 * @pattern delegate
 * @author dani
 */
public class DelegatedContainer implements Container {
    private final Container delegate;

    /**
     * Creates a delegated container with the given delegate
     * 
     * @param delegate
     */
    public DelegatedContainer(final Container delegate) {
	this.delegate = delegate;
    }

    public <T> T getInstance(final Class<T> componentType) {
	return delegate.getInstance(componentType);
    }

    public <T> Provider<T> getProvider(final Class<T> componentKey) {
	return delegate.getProvider(componentKey);
    }

    public <T> T register(final Class<T> componentType, final T component) {
	return delegate.register(componentType, component);
    }

    public <T> Provider<T> registerProvider(final Class<T> componentKey, final Provider<T> provider) {
	return delegate.registerProvider(componentKey, provider);
    }

}
