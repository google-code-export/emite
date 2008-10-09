package com.calclab.emite.widgets.habla.client;

import com.calclab.emite.browser.client.DomAssist;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

public class HablaModule extends AbstractModule implements EntryPoint {

    public void onModuleLoad() {
	Suco.install(this);
	Suco.get(Habla.class).deploy();
    }

    @Override
    protected void onInstall() {
	register(Singleton.class, new Factory<Habla>(Habla.class) {
	    @Override
	    public Habla create() {
		return new Habla($(DomAssist.class), $$(HablaWidget.class));
	    }
	}, new Factory<HablaWidget>(HablaWidget.class) {
	    @Override
	    public HablaWidget create() {
		return new HablaWidget();
	    }

	    @Override
	    public void onAfterCreated(final HablaWidget instance) {
		new HablaController($(Session.class), $(ChatManager.class), instance);
	    }
	});
    }

}
