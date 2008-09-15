package com.calclab.emite.core.client;

import com.calclab.emite.core.client.bosh.Bosh3Connection;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.services.gwt.GWTServices;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionScope;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scope.SingletonScope;
import com.google.gwt.core.client.EntryPoint;

public class EmiteCoreModule extends AbstractModule implements EntryPoint {

    public EmiteCoreModule() {
    }

    @Override
    public void onLoad() {
	registerScope(SessionScope.class, new SessionScope());

	register(SingletonScope.class, new Factory<Services>(Services.class) {
	    public Services create() {
		return new GWTServices();
	    }
	});

	register(SingletonScope.class, new Factory<Connection>(Connection.class) {
	    public Connection create() {
		return new Bosh3Connection($(Services.class));
	    }
	});

	final Factory<Session> sessionFactory = new Factory<Session>(Session.class) {
	    public Session create() {
		final XmppSession session = new XmppSession($(Connection.class), $(SASLManager.class),
			$(ResourceBindingManager.class));
		return session;
	    }
	};
	sessionFactory.onAfterCreated(new Listener<Session>() {
	    public void onEvent(final Session session) {
		final SessionScope sessionScope = $(SessionScope.class);
		sessionScope.setContext(session);
		sessionScope.createAll();
	    }
	});

	register(SingletonScope.class, new Factory<ResourceBindingManager>(ResourceBindingManager.class) {
	    public ResourceBindingManager create() {
		return new ResourceBindingManager($(Connection.class));
	    }
	}, new Factory<SASLManager>(SASLManager.class) {
	    public SASLManager create() {
		return new SASLManager($(Connection.class));
	    }
	}, sessionFactory);

	register(SingletonScope.class, new Factory<Xmpp>(Xmpp.class) {
	    public Xmpp create() {
		return new Xmpp($(Container.class));
	    }
	});
    }

    public void onModuleLoad() {
	Suco.install(this);
    }

}
