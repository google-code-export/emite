package com.calclab.examples.emite.echo.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

public class EchoModule extends AbstractModule implements EntryPoint {
    /**
     * Called by GWT when this module is loaded in a browser
     */
    public void onModuleLoad() {
	// install this module in suco
	Suco.install(this);
    }

    /**
     * Called by Suco when this module is installed in Suco
     */
    @Override
    protected void onInstall() {
	// The SessionComponent decorator take care of calling the factories
	// when a Session is created (see Suco for more info)
	register(SessionComponent.class, new Factory<Echo>(Echo.class) {
	    @Override
	    public Echo create() {
		return new Echo($(Session.class));
	    }
	});
    }
}
