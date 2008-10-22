package com.calclab.emite.browser.client;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

/**
 * Browser utility support
 */
public class BrowserModule extends AbstractModule implements EntryPoint {

    public BrowserModule() {
	super();
    }

    public void onModuleLoad() {
	Suco.install(this);
	Suco.get(AutoConfig.class).run();
    }

    @Override
    protected void onInstall() {
	register(Singleton.class, new Factory<DomAssist>(DomAssist.class) {
	    @Override
	    public DomAssist create() {
		return new DomAssist();
	    }
	}, new Factory<AutoConfig>(AutoConfig.class) {
	    @Override
	    public AutoConfig create() {
		return new AutoConfig($(Connection.class), $(Session.class), $(DomAssist.class));
	    }
	});
    }

}
