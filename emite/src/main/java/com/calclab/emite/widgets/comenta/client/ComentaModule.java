package com.calclab.emite.widgets.comenta.client;

import com.calclab.emite.browser.client.DomAssist;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

public class ComentaModule extends AbstractModule implements EntryPoint {
    public void onModuleLoad() {
	Suco.install(this);
	Suco.get(Comenta.class).deploy();
    }

    @Override
    protected void onLoad() {
	register(Singleton.class, new Factory<Comenta>(Comenta.class) {
	    @Override
	    public Comenta create() {
		return new Comenta($(DomAssist.class), $$(ComentaWidget.class));
	    }
	}, new Factory<ComentaWidget>(ComentaWidget.class) {
	    @Override
	    public ComentaWidget create() {
		final ComentaWidget widget = new ComentaWidget();
		return $(ComentaController.class).setWidget(widget);
	    }
	}, new Factory<ComentaController>(ComentaController.class) {
	    @Override
	    public ComentaController create() {
		return new ComentaController($(Session.class));
	    }
	});
    }

}
