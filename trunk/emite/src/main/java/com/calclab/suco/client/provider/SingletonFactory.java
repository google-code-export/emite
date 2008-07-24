package com.calclab.suco.client.provider;

import com.calclab.suco.client.scopes.SingletonScope;

public abstract class SingletonFactory<T> extends AbstractFactory<T> {
    public SingletonFactory(final Class<T> type) {
	super(type, SingletonScope.class);
    }
}
