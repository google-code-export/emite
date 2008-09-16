package com.calclab.emite.core.client;

import com.calclab.emite.core.client.bosh.Bosh3Connection;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.services.gwt.GWTServices;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionListener;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

public class EmiteCoreModule extends AbstractModule implements EntryPoint {

    public EmiteCoreModule() {
    }

    @Override
    public void onLoad() {
	registerDecorator(SessionListener.class, new SessionListener());

	register(Singleton.class, new Factory<Services>(Services.class) {
	    @Override
	    public Services create() {
		return new GWTServices();
	    }
	});

	register(Singleton.class, new Factory<Connection>(Connection.class) {
	    @Override
	    public Connection create() {
		return new Bosh3Connection($(Services.class));
	    }
	}, new Factory<Session>(Session.class) {
	    @Override
	    public Session create() {
		final XmppSession session = new XmppSession($(Connection.class), $(SASLManager.class),
			$(ResourceBindingManager.class));
		return session;
	    }

	    @Override
	    public void onAfterCreated(final Session session) {
		final SessionListener sessionListener = $(SessionListener.class);
		sessionListener.createAll();
	    }
	}, new Factory<ResourceBindingManager>(ResourceBindingManager.class) {
	    @Override
	    public ResourceBindingManager create() {
		return new ResourceBindingManager($(Connection.class));
	    }
	}, new Factory<SASLManager>(SASLManager.class) {
	    @Override
	    public SASLManager create() {
		return new SASLManager($(Connection.class));
	    }
	});

    }

    public void onModuleLoad() {
	Suco.install(this);
    }

}
