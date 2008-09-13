package com.calclab.emite.client.browser;

import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.xmpp.session.Session;
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
