package com.calclab.suco.client.provider;

import com.calclab.suco.client.scopes.Scope;

public interface Factory<T> {
    public T create();

    public Class<? extends Scope> getScope();

    public Class<T> getType();
}
