package com.calclab.emite.client.core.bosh;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.components.AbstractComponent;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.connector.ConnectorCallback;
import com.calclab.emite.client.connector.ConnectorException;
import com.calclab.emite.client.core.dispatcher.Action;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.XMLService;
import com.calclab.emite.client.scheduler.Scheduler;
import com.calclab.emite.client.x.core.SASLManager;
import com.calclab.emite.client.x.im.session.Session;

public class Bosh extends AbstractComponent implements Connection, ConnectorCallback {

	private Activator activator;
	private final Connector connector;
	private final Dispatcher dispatcher;
	private final BoshOptions options;
	private Body request;
	private final Scheduler scheduler;
	private final BoshState state;
	private final XMLService xmler;
	final Action publishStanzas;
	final Action restartStream;
	final Action send;
	final Action sendCreation;
	final Action stop;

	public Bosh(final Dispatcher dispatcher, final Globals globals, final Connector connector, final XMLService xmler,
			final Scheduler scheduler, final BoshOptions options) {
		super(dispatcher);
		this.dispatcher = dispatcher;
		this.connector = connector;
		this.xmler = xmler;
		this.scheduler = scheduler;
		this.options = options;
		this.activator = new Activator(this);
		this.state = new BoshState();

		globals.setDomain(options.getDomain());
		globals.setResourceName(options.getResource());

		restartStream = new Action() {
			public void handle(final Packet received) {
				setRestart();
			}
		};

		sendCreation = new Action() {
			public void handle(final Packet received) {
				state.setRunning(true);
				request.setCreationState(options.getDomain());
			}
		};

		stop = new Action() {
			public void handle(final Packet stanza) {
				stop();
			}
		};

		send = new Action() {
			public void handle(final Packet received) {
				final List<? extends Packet> children = received.getChildren();
				for (final Packet child : children) {
					send(child);
				}
			}
		};

		/**
		 * a body element was published into the dispatcher
		 */
		publishStanzas = new Action() {
			public void handle(final Packet packet) {
				final Body response = new Body(packet);
				publishBodyChilds(response);
			}
		};

	}

	@Override
	public void attach() {
		when(SASLManager.Events.authorized).Do(restartStream);
		when(Connection.Events.start).Do(sendCreation);
		when(Connection.Events.error).Do(stop);
		when(Connection.Events.send).Do(send);
		when("body").Do(publishStanzas);
		when(Session.Events.logout).Do(new Action() {
			public void handle(final Packet received) {
				setTerminate();
			}
		});
	}

	public void catchPackets() {
		if (this.request == null) {
			request = new Body(state.nextRid(), state.getSID());
		}
	}

	public void firePackets() {
		activator.cancel();
		if (state.isRunning()) {
			if (request.isEmpty() && state.isLastResponseEmpty()) {
				if (state.getCurrentRequestsCount() == 0) {
					delaySend();
				}
			} else {
				sendResponse();
			}
		} else {
			Log.debug("FIRE PACKETS CANCELLED BECAUSE WE'RE STOPPED");
		}
	}

	public void onError(final Throwable throwable) {
		state.decreaseRequests();
		dispatcher.publish(Connection.Events.error);
	}

	/**
	 * An incoming body from the server
	 * 
	 * @see ConnectorCallback
	 */
	public void onResponseReceived(final int statusCode, final String content) {
		state.decreaseRequests();
		if (statusCode >= 400) {
			dispatcher.publish(Connection.Events.error);
		} else {
			final Packet response = xmler.toXML(content);
			if ("body".equals(response.getName())) {
				dispatcher.publish(response);
			} else {
				dispatcher.publish(Connection.Events.error);
			}
		}
	}

	public void send(final Packet toBeSend) {
		Log.debug("BOSH::Queueing: " + toBeSend);
		request.addChild(toBeSend);
	}

	public void setRestart() {
		request.setRestart(options.getDomain());
	}

	public void setTerminate() {
		request.setTerminate();
		state.setTerminate();
	}

	public void stop() {
		state.init();
	}

	private void delaySend() {
		this.activator = new Activator(this);
		scheduler.schedule(state.getPoll(), activator);
	}

	private void publishBodyChilds(final Body response) {
		if (state.isFirstResponse()) {
			final String sid = response.getSID();
			state.setSID(sid);
			request.setSID(sid);
			state.setPoll(response.getPollingMilisecs());
		}
		state.setLastResponseEmpty(response.isEmpty());
		if (response.isTerminal()) {
			dispatcher.publish(new Event(Connection.Events.error).Because(response.getCondition()));
		} else {
			final List<? extends Packet> children = response.getChildren();
			for (final Packet stanza : children) {
				dispatcher.publish(stanza);
			}
		}
	}

	void sendResponse() {
		try {
			connector.send(options.getHttpBase(), xmler.toString(request), this);
			request = null;
			state.setLastSend(scheduler.getCurrentTime());
			state.increaseRequests();
		} catch (final ConnectorException e) {
			dispatcher.publish(Connection.Events.error);
		}
	}

}
