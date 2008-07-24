package com.calclab.suco.client.modules;

import com.calclab.emite.client.xmpp.session.SessionScope;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.provider.AbstractFactory;
import com.calclab.suco.client.scopes.Scope;
import com.calclab.suco.client.scopes.Scopes;
import com.calclab.suco.client.scopes.SingletonScope;

public abstract class AbstractModule implements Module {

    private final Class<?> type;
    private ModuleBuilder builder;

    public AbstractModule(final Class<?> moduleType) {
	this.type = moduleType;
    }

    public <T> T $(final Class<T> componentType) {
	return builder.getInstance(componentType);
    }

    public Class<?> getType() {
	return type;
    }

    public abstract void onLoad();

    public void onLoad(final ModuleBuilder builder) {
	this.builder = builder;
	onLoad();
    }

    public void register(final AbstractFactory<?>... providerInfos) {
	for (final AbstractFactory<?> factory : providerInfos) {
	    register(factory);
	}
    }

    public <T> void register(final AbstractFactory<T> provider) {
	builder.registerProvider(provider.getType(), provider.getProvider());
    }

    public void registerProvider(final Class<SessionScope> type, final Provider<SessionScope> provider,
	    final Class<SingletonScope> scopeType) {
	builder.registerProvider(type, provider, scopeType);

    }

    public <S extends Scope> void registerScope(final Class<S> scopeType, final S scope) {
	Scopes.addScope(scopeType, scope);
	builder.registerProvider(scopeType, Scopes.getProvider(scopeType), SingletonScope.class);
    }
}
