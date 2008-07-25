package com.calclab.suco.client.provider;

import com.calclab.suco.client.container.Provider;

public abstract class Factory<T> implements FactoryProvider<T>, Provider<T> {
    private final Class<T> type;

    public Factory(final Class<T> type) {
	this.type = type;
    }

    public final T get() {
	return create();
    }

    public final Class<T> getType() {
	return type;
    }

}
