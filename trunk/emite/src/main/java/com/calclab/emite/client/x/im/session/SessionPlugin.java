package com.calclab.emite.client.x.im.session;

import com.calclab.emite.client.IContainer;
import com.calclab.emite.client.IDispatcher;
import com.calclab.emite.client.IGlobals;
import com.calclab.emite.client.action.BussinessLogic;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.plugin.FilterBuilder;
import com.calclab.emite.client.plugin.Plugin2;
import com.calclab.emite.client.x.core.ResourceModule;

/**
 * TODO: better plugin system!!!
 * 
 * @author dani
 * 
 */
public class SessionPlugin implements Plugin2 {
	public static class Events {
		public static final Event ended = new Event("session:ended");
		public static final Event started = new Event("session:success");
	}

	public static Session getSession(final IContainer components) {
		return (Session) components.get("session");
	}

	private final Session session;
	final BussinessLogic requestSession;

	public SessionPlugin(final IGlobals globals, final IDispatcher dispatcher) {
		session = new Session(globals, dispatcher);

		requestSession = new BussinessLogic() {
			public Packet logic(final Packet received) {
				return new IQ("requestSession", IQ.Type.set).To(globals.getDomain()).Include("session", "");
			}
		};
	}

	public void start(final FilterBuilder when, final IContainer components) {
		components.register("session", session);

		when.Event(ResourceModule.Events.binded).send(requestSession);

		when.IQ("sess_1").publish(SessionPlugin.Events.started);

	}
}
