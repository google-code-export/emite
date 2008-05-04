package com.calclab.emite.examples.echo;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.core.bosh.Emite;

public class EchoModule {

    private static final String COMPONENT_ECHO = "echo:echo";

    public static Echo getEcho(final Container container) {
	return (Echo) container.get(COMPONENT_ECHO);
    }

    public static void install(final Container container) {
	final Emite emite = CoreModule.getEmite(container);
	final Echo echo = new Echo(emite);
	container.register(COMPONENT_ECHO, echo);
    }
}
