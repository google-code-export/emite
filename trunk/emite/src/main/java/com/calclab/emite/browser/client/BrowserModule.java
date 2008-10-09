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
	Suco.get(PageController.class).init();
    }

    @Override
    protected void onInstall() {
	register(Singleton.class, new Factory<DomAssist>(DomAssist.class) {
	    @Override
	    public DomAssist create() {
		return new DomAssist();
	    }
	}, new Factory<PageController>(PageController.class) {
	    @Override
	    public PageController create() {
		return new PageController($(Connection.class), $(Session.class), $(DomAssist.class));
	    }
	});
    }

}
