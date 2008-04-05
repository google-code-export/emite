package com.calclab.emite.client.components;


public class ContainerPlugin {
    private static final String COMPONENT_GLOBALS = "globals";

    public static Container create() {
	final DefaultContainer container = new DefaultContainer();
	container.register(COMPONENT_GLOBALS, new HashGlobals());
	return container;
    }

    public static Globals getGlobals(final Container container) {
	return (Globals) container.get(COMPONENT_GLOBALS);

    }
}
