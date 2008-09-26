package com.calclab.emite.core.client;

import com.calclab.emite.core.client.bosh.BoshConnection;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.services.gwt.GWTServices;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.InitialPresence;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.emite.core.client.xmpp.session.SessionImpl;
import com.calclab.emite.core.client.xmpp.session.IMSessionManager;
import com.calclab.emite.core.client.xmpp.session.SessionReadyManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.calclab.suco.client.log.Logger;
import com.google.gwt.core.client.EntryPoint;

public class EmiteCoreModule extends AbstractModule implements EntryPoint {

    public EmiteCoreModule() {
    }

    @Override
    public void onLoad() {
	registerDecorator(SessionComponent.class, new SessionComponent(container));

	register(Singleton.class, new Factory<Services>(Services.class) {
	    @Override
	    public Services create() {
		return new GWTServices();
	    }
	});

	register(Singleton.class, new Factory<Connection>(Connection.class) {
	    @Override
	    public Connection create() {
		return new BoshConnection($(Services.class));
	    }
	}, new Factory<IMSessionManager>(IMSessionManager.class) {
	    @Override
	    public IMSessionManager create() {
		return new IMSessionManager($(Connection.class));
	    }
	}, new Factory<Session>(Session.class) {
	    @Override
	    public Session create() {
		final SessionImpl session = new SessionImpl($(Connection.class), $(SASLManager.class),
			$(ResourceBindingManager.class), $(IMSessionManager.class), $(SessionReadyManager.class));
		return session;
	    }

	    @Override
	    public void onAfterCreated(final Session session) {
		Logger.debug("Creating Session grouped objects...");
		$(SessionComponent.class).createAll();
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
	register(SessionComponent.class, new Factory<SessionReadyManager>(SessionReadyManager.class) {
	    @Override
	    public SessionReadyManager create() {
		return new InitialPresence($(Session.class));
	    }
	});

    }

    public void onModuleLoad() {
	Suco.install(this);
    }

}
