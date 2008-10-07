package com.calclab.emite.core.client.xmpp.session;

import com.calclab.suco.client.ioc.Container;
import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.ioc.decorator.ProviderCollection;
import com.calclab.suco.client.ioc.decorator.Singleton;

/**
 * All the providers/factories decorated by SessionComponent are called when a
 * new Session component is created.
 * 
 * Use this decorator if your component DEPENDS on Session to perform its tasks
 */
public class SessionComponent extends ProviderCollection {

    public SessionComponent(final Container container) {
	super(container, Singleton.instance);
    }

    public void createAll() {
	for (final Provider<?> p : getProviders()) {
	    p.get();
	}
    }
}
