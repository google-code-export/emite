package com.calclab.emite.client.modular;

import com.calclab.emite.client.modular.Container;

public class DelegatedContainer implements Container {
    private final Container delegate;

    public DelegatedContainer(final Container delegate) {
	this.delegate = delegate;
    }

    public <T> T get(final Class<T> componentType) {
	return delegate.get(componentType);
    }

    public <T> T register(final Class<T> componentType, final T component) {
	return delegate.register(componentType, component);
    }

}
