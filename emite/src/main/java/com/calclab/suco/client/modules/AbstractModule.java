package com.calclab.suco.client.modules;

import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.provider.FactoryProvider;
import com.calclab.suco.client.scopes.Scope;
import com.calclab.suco.client.scopes.Scopes;
import com.calclab.suco.client.scopes.SingletonScope;

public abstract class AbstractModule implements Module {

    private final Class<? extends Module> type;
    private Container container;

    public AbstractModule(final Class<? extends Module> moduleType) {
	this.type = moduleType;
    }

    public Class<? extends Module> getType() {
	return type;
    }

    public void onLoad(final Container container) {
	this.container = container;
	onLoad();
    }

    /**
     * Get a instance of the specified component key
     * 
     * @param <T>
     *            The component key
     * @param componentType
     *            The component key
     * @return The component instance
     */
    protected <T> T $(final Class<T> componentType) {
	return container.getInstance(componentType);
    }

    /**
     * Get a provider of the specified component key
     * 
     * @param <T>
     *            The component key
     * @param componentType
     *            The component key
     * @return The provider of that component key
     */
    protected <T> Provider<T> $$(final Class<T> componentType) {
	return container.getProvider(componentType);
    }

    protected void install(final Module... modules) {
	container.getInstance(ModuleManager.class).install(modules);
    }

    /**
     * @deprecated Use install method
     */
    @Deprecated
    protected void load(final Module... modules) {
	container.getInstance(ModuleManager.class).install(modules);
    }

    protected abstract void onLoad();

    protected void register(final Class<? extends Scope> scopeType, final FactoryProvider<?>... providerInfos) {
	for (final FactoryProvider<?> factory : providerInfos) {
	    registerFactory(scopeType, factory);
	}
    }

    protected <O> void registerProvider(final Class<O> type, final Provider<O> provider,
	    final Class<? extends Scope> scopeType) {
	final Scope scope = Scopes.get(scopeType);
	container.registerProvider(type, scope.scope(type, provider));
    }

    protected <S extends Scope> void registerScope(final Class<S> scopeType, final S scope) {
	Scopes.addScope(scopeType, scope);
	registerProvider(scopeType, Scopes.getProvider(scopeType), SingletonScope.class);
    }

    private <O> void registerFactory(final Class<? extends Scope> scopeType, final FactoryProvider<O> factory) {
	registerProvider(factory.getType(), factory, scopeType);
    }
}
