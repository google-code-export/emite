package com.calclab.emite.browser.client;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scope.SingletonScope;

/**
 * Browser utility support
 */
public class BrowserModule extends AbstractModule {

    public BrowserModule() {
	super();
    }

    @Override
    protected void onLoad() {
	register(SingletonScope.class, new Factory<DomAssist>(DomAssist.class) {
	    public DomAssist create() {
		return new DomAssist();
	    }
	}, new Factory<PageController>(PageController.class) {
	    public PageController create() {
		return new PageController($(Connection.class), $(Session.class), $(DomAssist.class));
	    }
	});
    }

}
