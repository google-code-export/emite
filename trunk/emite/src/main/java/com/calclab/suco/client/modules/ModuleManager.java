package com.calclab.suco.client.modules;

import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.DelegatedContainer;
import com.calclab.suco.client.container.OverrideableContainer;
import com.calclab.suco.client.container.Provider;

public class ModuleManager {

    public static enum ProviderRegisterStrategy {
	/**
	 * Register the provider always: override if previously registered
	 */
	registerOrOverride,
	/**
	 * Fail if try to register a provider twice
	 */
	failIfRegistered,
	/**
	 * Register the provider only if not previously registered
	 */
	registerOnlyIfNotRegistered
    }

    private final OverrideableContainer overrideable;
    private final DelegatedContainer notOverrideable;
    private final DelegatedContainer conditional;

    public ModuleManager(final OverrideableContainer container) {
	this.overrideable = container;
	this.notOverrideable = new DelegatedContainer(container) {
	    @Override
	    public <T> Provider<T> registerProvider(final Class<T> componentKey, final Provider<T> provider) {
		if (delegate.hasProvider(componentKey)) {
		    throw new RuntimeException("Provider " + componentKey + " already registered");
		} else {
		    return super.registerProvider(componentKey, provider);
		}
	    }
	};
	this.conditional = new DelegatedContainer(container) {
	    @Override
	    public <T> Provider<T> registerProvider(final Class<T> componentKey, final Provider<T> provider) {
		if (!delegate.hasProvider(componentKey)) {
		    return super.registerProvider(componentKey, provider);
		} else {
		    return getProvider(componentKey);
		}
	    }
	};
    }

    public Container getContainer() {
	return overrideable;
    }

    public void install(final Module module, final ProviderRegisterStrategy registerStrategy) {
	switch (registerStrategy) {
	case failIfRegistered:
	    install(module, notOverrideable);
	    break;
	case registerOrOverride:
	    install(module, overrideable);
	    break;
	case registerOnlyIfNotRegistered:
	    install(module, conditional);
	}
    }

    public void install(final ProviderRegisterStrategy registerStrategy, final Module... modules) {
	for (final Module module : modules) {
	    install(module, registerStrategy);
	}
    }

    private void install(final Module module, final Container aContainer) {
	module.onLoad(aContainer);
    }

}
