package com.calclab.emite.client.x.im.session;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.Globals;
import com.calclab.emite.client.action.BussinessLogic;
import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.plugin.FilterBuilder;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.x.core.ResourceModule;
import com.calclab.emite.client.x.core.SASLModule;

/**
 * TODO: better plugin system!!!
 * 
 * @author dani
 * 
 */
public class SessionPlugin implements Plugin {
	public static class Events {
		public static final Event ended = new Event("session:ended");
		public static final Event started = new Event("session:started");
	}

	public static Session getSession(final Components components) {
		return (Session) components.get("session");
	}

	private final Session session;
	final BussinessLogic requestSession;
	final BussinessLogic setAuthorizedState;
	final BussinessLogic setStartedState;
	final BussinessLogic startConnection;

	public SessionPlugin(final Globals globals, final Dispatcher dispatcher, final IConnection connection) {
		session = new Session(globals, dispatcher);

		requestSession = new BussinessLogic() {
			public Packet logic(final Packet received) {
				return new IQ("requestSession", IQ.Type.set).To(globals.getDomain()).Include("session", "");
			}
		};

		startConnection = new BussinessLogic() {
			public Packet logic(final Packet received) {
				connection.start();
				return null;
			}
		};

		setAuthorizedState = new BussinessLogic() {
			public Packet logic(final Packet received) {
				session.setState(Session.State.authorized);
				return null;
			}
		};

		setStartedState = new BussinessLogic() {
			public Packet logic(final Packet received) {
				session.setState(Session.State.connected);
				return Events.started;
			}
		};
	}

	public void start(final FilterBuilder when, final Components components) {
		components.register("session", session);

		when.Event(Session.Events.login).Do(startConnection);

		when.Event(SASLModule.Events.authorized).Do(setAuthorizedState);

		when.Event(ResourceModule.Events.binded).send(requestSession);

		when.IQ("requestSession").publish(setStartedState);

	}
}
