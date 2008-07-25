package com.calclab.suco.client.provider;

import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.scopes.Scope;
import com.calclab.suco.client.scopes.Scopes;
import com.calclab.suco.client.scopes.SingletonScope;

public abstract class AbstractFactory<T> implements FactoryProvider<T>, Provider<T> {
    private final Class<T> type;
    private final Class<? extends Scope> scope;

    public AbstractFactory(final Class<T> type, final Class<? extends Scope> scope) {
	this.type = type;
	this.scope = scope;
    }

    public final T get() {
	return create();
    }

    public Provider<T> getProvider() {
	return Scopes.get(scope).scope(type, this);
    }

    public final Class<? extends Scope> getScope() {
	return SingletonScope.class;
    }

    public final Class<T> getType() {
	return type;
    }

}
