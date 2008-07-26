package com.calclab.emite.widgets.demo.client;

import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.widgets.client.deploy.Installer;
import com.calclab.emite.widgets.client.room.ComentaWidget;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scopes.NoScope;
import com.calclab.suco.client.scopes.SingletonScope;

public class ComentaModule extends AbstractModule {
    public ComentaModule() {
	super(ComentaModule.class);
    }

    @Override
    protected void onLoad() {
	register(SingletonScope.class, new Factory<Installer>(Installer.class) {
	    public Installer create() {
		return new Installer();
	    }
	});

	register(NoScope.class, new Factory<ComentaWidget>(ComentaWidget.class) {
	    public ComentaWidget create() {
		final ComentaWidget widget = new ComentaWidget();
		$(ComentaController.class).setWidget(widget);
		return widget;
	    }
	}, new Factory<ComentaController>(ComentaController.class) {
	    public ComentaController create() {
		return new ComentaController($(Session.class));
	    }
	});
    }

}
