package com.calclab.suco.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.OverrideableContainer;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.Module;
import com.calclab.suco.client.modules.ModuleManager;
import com.calclab.suco.client.modules.ModuleManager.ProviderRegisterStrategy;

public class Suco {

    public static Container create(final Module... modules) {
	Log.debug("NEW SUCO!");
	final OverrideableContainer container = createContainer();
	createModuleManager(container);
	install(container, ProviderRegisterStrategy.failIfRegistered, modules);
	return container;
    }

    public static void install(final Container container, final ProviderRegisterStrategy strategy,
	    final Module... modules) {
	container.getInstance(ModuleManager.class).install(strategy, modules);
    }

    private static OverrideableContainer createContainer() {
	final OverrideableContainer container = new OverrideableContainer();
	container.registerProvider(Container.class, new Provider<Container>() {
	    public Container get() {
		return container;
	    }
	});
	return container;
    }

    private static void createModuleManager(final OverrideableContainer container) {
	final ModuleManager manager = new ModuleManager(container);
	container.registerProvider(ModuleManager.class, new Provider<ModuleManager>() {
	    public ModuleManager get() {
		return manager;
	    }
	});
    }

}
