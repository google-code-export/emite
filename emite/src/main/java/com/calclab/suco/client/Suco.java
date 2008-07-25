package com.calclab.suco.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.HashContainer;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.Module;

public class Suco {

    public static void add(final Container container, final Module... modules) {
	for (final Module m : modules) {
	    m.onLoad(container);
	}
    }

    public static Container create(final Module... modules) {
	Log.debug("NEW SUCO!");
	final HashContainer container = new HashContainer();
	container.registerProvider(Container.class, new Provider<Container>() {
	    public Container get() {
		return container;
	    }
	});
	add(container, modules);
	return container;
    }

}
