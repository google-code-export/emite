package com.calclab.suco.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.HashContainer;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.Module;
import com.calclab.suco.client.modules.ModuleManager;

public class Suco {

    public static Container create(final Module... modules) {
	Log.debug("NEW SUCO!");
	final HashContainer container = new HashContainer();
	final ModuleManager installer = new ModuleManager(container);
	container.registerProvider(Container.class, new Provider<Container>() {
	    public Container get() {
		return container;
	    }
	});
	container.registerProvider(ModuleManager.class, new Provider<ModuleManager>() {
	    public ModuleManager get() {
		return installer;
	    }
	});
	install(container, modules);
	return container;
    }

    public static void install(final Container container, final Module... modules) {
	container.getInstance(ModuleManager.class).install(modules);
    }

}
