package com.calclab.emite.client.core.bosh;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.dispatcher.Action;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.PublisherComponent;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Connector;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.Scheduler;
import com.calclab.emite.client.core.services.XMLService;

public class BoshManager extends PublisherComponent implements Bosh, ConnectorCallback {

	private Activator activator;
	private final Connector connector;
	private final Dispatcher dispatcher;
	private final BoshOptions options;
	private Body request;
	private final Scheduler scheduler;
	private final BoshState state;
	private final XMLService xmler;
	final Action onBodyReceived;
	final Action restartStream;
	final Action send;
	final Action sendCreation;
	final Action stop;

	public BoshManager(final Dispatcher dispatcher, final Globals globals, final Connector connector,
			final XMLService xmler, final Scheduler scheduler, final BoshOptions options) {
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
		onBodyReceived = new Action() {
			public void handle(final Packet packet) {
				final Body response = new Body(packet);
				publishBodyChilds(response);
			}
		};

	}

	@Override
	public void attach() {
		when(Bosh.Events.restart).Do(restartStream);
		when(Bosh.Events.start).Do(sendCreation);
		when(Bosh.Events.error).Do(stop);
		when(Bosh.Events.send).Do(send);
		when("body").Do(onBodyReceived);
		when(Bosh.Events.stop).Do(new Action() {
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
			if (request.isEmpty()) {
				if (state.getCurrentRequestsCount() > 0) {
					// no need
				} else {
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
		dispatcher.publish(Bosh.Events.error);
	}

	/**
	 * An incoming body from the server
	 * 
	 * @see ConnectorCallback
	 */
	public void onResponseReceived(final int statusCode, final String content) {
		state.decreaseRequests();
		if (statusCode >= 400) {
			dispatcher.publish(Bosh.Events.error);
		} else {
			final Packet response = xmler.toXML(content);
			if ("body".equals(response.getName())) {
				dispatcher.publish(response);
			} else {
				dispatcher.publish(Bosh.Events.error);
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
		state.setTerminating();
	}

	@Override
	public void stop() {
		state.setRunning(false);
	}

	private void delaySend() {
		this.activator = new Activator(this);
		final int ms = state.getPoll();
		final int diference = (int) (scheduler.getCurrentTime() - state.getLastSendTime());
		int total = ms - diference;
		Log.debug("DELAYING - poll: " + ms + ", diff: " + diference + ", total: " + total);
		if (total < 1) {
			total = 1;
		}
		scheduler.schedule(total, activator);
	}

	private void publishBodyChilds(final Body response) {
		if (state.isTerminating()) {
			stop();
		} else if (state.isFirstResponse()) {
			final String sid = response.getSID();
			state.setSID(sid);
			request.setSID(sid);
			state.setPoll(response.getPoll() + 500);
		}
		state.setLastResponseEmpty(response.isEmpty());
		if (response.isTerminal()) {
			dispatcher.publish(new Event(Bosh.Events.error).Because(response.getCondition()));
		} else {
			final List<? extends Packet> children = response.getChildren();
			for (final Packet stanza : children) {
				dispatcher.publish(stanza);
			}
		}
	}

	void sendResponse() {
		try {
			Log.debug("SENDING. Current: " + state.getCurrentRequestsCount() + ", after: "
					+ (scheduler.getCurrentTime() - state.getLastSendTime()));
			connector.send(options.getHttpBase(), xmler.toString(request), this);
			request = null;
			final long now = scheduler.getCurrentTime();
			final long last = state.getLastSendTime();
			Log.debug("BOSH SEND: " + last + " -> " + now + "(" + (now - last) + ")");
			state.setLastSend(now);
			state.increaseRequests();
		} catch (final ConnectorException e) {
			dispatcher.publish(Bosh.Events.error);
		}
	}

}
