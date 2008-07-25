package com.calclab.suco.client.provider;

import com.calclab.suco.client.container.Provider;

public interface FactoryProvider<T> extends Provider<T> {
    public T create();

    public Class<T> getType();
}
