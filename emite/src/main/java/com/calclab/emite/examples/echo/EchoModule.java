package com.calclab.emite.examples.echo;

import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.modular.Container;

public class EchoModule {
    private static final Class<Echo> COMPONENT_ECHO = Echo.class;

    public static Echo getEcho(final Container container) {
	return container.getInstance(COMPONENT_ECHO);
    }

    public static void install(final Container container) {
	final Emite emite = CoreModule.getEmite(container);
	final Echo echo = new Echo(emite);
	container.register(COMPONENT_ECHO, echo);
    }
}
