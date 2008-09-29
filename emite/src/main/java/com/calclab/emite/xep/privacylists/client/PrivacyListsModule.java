package com.calclab.emite.xep.privacylists.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

/**
 * Future implementation of: http://www.xmpp.org/extensions/xep-0016.html
 * 
 * Only one component: PrivacyListManager
 * 
 * @see PrivacyListsManager
 */
public class PrivacyListsModule extends AbstractModule implements EntryPoint {

    public void onModuleLoad() {
	register(SessionComponent.class, new Factory<PrivacyListsManager>(PrivacyListsManager.class) {
	    @Override
	    public PrivacyListsManager create() {
		return new PrivacyListsManager($(Session.class));
	    }
	});
    }

    @Override
    protected void onLoad() {
	Suco.install(this);
    }

}
