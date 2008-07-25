package com.calclab.suco.client.modules;

import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.provider.FactoryProvider;
import com.calclab.suco.client.scopes.Scope;
import com.calclab.suco.client.scopes.Scopes;
import com.calclab.suco.client.scopes.SingletonScope;

public abstract class AbstractModule implements Module {

    private final Class<?> type;
    private Container container;

    public AbstractModule(final Class<?> moduleType) {
	this.type = moduleType;
    }

    public <T> T $(final Class<T> componentType) {
	return container.getInstance(componentType);
    }

    public <T> Provider<T> $p(final Class<T> componentType) {
	return container.getProvider(componentType);
    }

    public Class<?> getType() {
	return type;
    }

    public abstract void onLoad();

    public void onLoad(final Container container) {
	this.container = container;
	onLoad();
    }

    public void register(final Class<? extends Scope> scopeType, final FactoryProvider<?>... providerInfos) {
	for (final FactoryProvider<?> factory : providerInfos) {
	    registerFactory(scopeType, factory);
	}
    }

    public <O> void registerProvider(final Class<O> type, final Provider<O> provider,
	    final Class<? extends Scope> scopeType) {
	final Scope scope = Scopes.get(scopeType);
	container.registerProvider(type, scope.scope(type, provider));
    }

    public <S extends Scope> void registerScope(final Class<S> scopeType, final S scope) {
	Scopes.addScope(scopeType, scope);
	registerProvider(scopeType, Scopes.getProvider(scopeType), SingletonScope.class);
    }

    private <O> void registerFactory(final Class<? extends Scope> scopeType, final FactoryProvider<O> factory) {
	registerProvider(factory.getType(), factory, scopeType);
    }
}
