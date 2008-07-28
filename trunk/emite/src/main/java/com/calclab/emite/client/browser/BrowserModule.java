package com.calclab.emite.client.browser;

import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scopes.SingletonScope;

/**
 * Browser utility support
 */
public class BrowserModule extends AbstractModule {

    public BrowserModule() {
	super(BrowserModule.class);
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
