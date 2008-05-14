package com.calclab.uimite.client.config;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;

public class XmppAutoConfigModule implements Module {

    public void onLoad(final Container container) {
	final Xmpp xmpp = container.getInstance(Xmpp.class);
	container.registerSingletonInstance(Configurator.class, new Configurator(xmpp));
    }
}
