package com.calclab.emite.client.bosh;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Globals;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.connector.ConnectorCallback;
import com.calclab.emite.client.connector.ConnectorException;
import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.BasicPacket;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.XMLService;

public class Bosh implements Connection {

	final Action stop;
	final Action restartStream;
	final Action sendCreation;
	final Action send;
	final Action publishStanzas;
	private Packet body;
	private final XMLService xmler;
	private final Connector connector;
	private final String httpBase;
	private final Dispatcher dispatcher;
	private boolean isRunning;
	// FIXME: gwt and long problems
	private long rid;
	private String sid;
	private int currentConnections;

	public Bosh(final Dispatcher dispatcher, final Globals globals,
			final Connector connector, final XMLService xmler,
			final BoshOptions options) {
		this.dispatcher = dispatcher;
		this.connector = connector;
		this.xmler = xmler;
		this.httpBase = options.getHttpBase();
		this.body = null;
		this.isRunning = false;
		this.rid = 0;
		this.sid = null;
		this.currentConnections = 0;

		globals.setDomain(options.getDomain());

		restartStream = new Action() {
			public void handle(final Packet received) {
				setRestart();
			}
		};

		sendCreation = new Action() {
			public void handle(final Packet received) {
				isRunning = true;
				rid = generateRID();
				body.With("content", "text/xml; charset=utf-8")
						.With("rid", rid).With("to", options.getDomain()).With(
								"secure", "true").With("ver", "1.6").With(
								"wait", "60").With("ack", "1")
						.With("hold", "1").With("xml:lang", "en");
			}
		};

		stop = new Action() {
			public void handle(final Packet stanza) {
				stop();
			}
		};

		send = new Action() {
			public void handle(final Packet received) {
				send(received.getFirstChild("message"));
			}
		};

		publishStanzas = new Action() {
			public void handle(final Packet received) {
				if (sid == null) {
					setSID(received.getAttribute("sid"));
				}
				if (received.hasAttribute("type", "terminate")) {
					dispatcher.publish(new Event(Connection.Events.error)
							.Because(received.getAttribute("condition")));
				} else {
					final List<? extends Packet> children = received
							.getChildren();
					for (final Packet stanza : children) {
						dispatcher.publish(stanza);
					}
				}
			}
		};

	}

	public void catchPackets() {
		rid++;
		this.body = new BasicPacket("body",
				"http://jabber.org/protocol/httpbind").With("rid", rid);
		if (sid != null) {
			body.setAttribute("sid", sid);
		}
	}

	public void firePackets() {
		if (body.getChildrenCount() == 0 && currentConnections > 0) {
			// its an empty answer from the server
			rid--;
		} else {
			send();
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void send(final Packet toBeSend) {
		Log.debug("BOSH::Queueing: " + toBeSend);
		body.addChild(toBeSend);
	}

	public void setRestart() {
		body.setAttribute("restart", "true");
	}

	public void setSID(final String sid) {
		Log.debug("Setting SID!" + sid);
		this.sid = sid;
		body.setAttribute("sid", sid);
	}

	public void stop() {
		isRunning = false;
		sid = null;
	}

	private long generateRID() {
		final long rid = (long) (Math.random() * 1245234);
		return rid;
	}

	private void send() {
		try {
			connector.send(httpBase, xmler.toString(body),
					new ConnectorCallback() {
						public void onError(final Throwable throwable) {
							currentConnections--;
							dispatcher.publish(Connection.Events.error);
						}

						public void onResponseReceived(final int statusCode,
								final String content) {
							currentConnections--;
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

					});
			currentConnections++;
			body = null;
		} catch (final ConnectorException e) {
			dispatcher.publish(Connection.Events.error);
		}
	}

}
