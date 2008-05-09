package com.calclab.uimite.client.config;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.container.Container;

public class XmppAutoConfigModule {

    public static void load(final Container container) {
	final Xmpp xmpp = container.get(Xmpp.class);
	container.register(Configurator.class, new Configurator(xmpp));
    }
}
