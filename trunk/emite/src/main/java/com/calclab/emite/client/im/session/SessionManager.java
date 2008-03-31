package com.calclab.emite.client.im.session;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.Bosh;
import com.calclab.emite.client.core.bosh.SenderComponent;
import com.calclab.emite.client.core.dispatcher.Action;
import com.calclab.emite.client.core.dispatcher.Answer;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;

public class SessionManager extends SenderComponent {
	public static Session getSession(final Container container) {
		return (Session) container.get("session");
	}

	final Answer requestSession;
	final Action setAuthorizedState;
	final Answer setSessionStarted;

	public SessionManager(final Dispatcher dispatcher, final Bosh bosh, final Globals globals, final Session session) {
		super(dispatcher, bosh);

		requestSession = new Answer() {
			public Packet respondTo(final Packet received) {
				final IQ iq = new IQ("requestSession", IQ.Type.set).From(globals.getJID()).To(globals.getDomain());
				iq.Include("session", "urn:ietf:params:xml:ns:xmpp-session");
				return iq;
			}
		};

		setAuthorizedState = new Action() {
			public void handle(final Packet received) {
				session.setState(Session.State.authorized);
			}
		};

		setSessionStarted = new Answer() {
			public Packet respondTo(final Packet received) {
				session.setState(Session.State.connected);
				return Session.Events.login;
			}
		};
	}

	@Override
	public void attach() {

		when(SASLManager.Events.authorized).Publish(Bosh.Events.restart);
		when(SASLManager.Events.authorized).Do(setAuthorizedState);

		when(Session.Events.logout).Publish(Bosh.Events.stop);

		when(ResourceBindingManager.Events.binded).Send(requestSession);
		when(new IQ("requestSession", IQ.Type.result, null)).Publish(setSessionStarted);

	}
}
