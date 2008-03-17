package com.calclab.emite.client;

import java.util.HashMap;

import com.calclab.emite.client.bosh.Bosh;
import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.log.LoggerAdapter;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.plugin.PluginManager;
import com.calclab.emite.client.subscriber.PacketSubscriber;

/**
 * FACADE
 * 
 * 
 * @author dani
 * 
 */
public class Engine {

	public static final String DOMAIN = "domain";
	public static final String JID = "jid";
	public static final String PASSWORD = "password";
	public static final String USER = "user";

	private final Bosh bosh;
	private final Dispatcher dispatcher;
	private final HashMap<String, Object> globals;
	private final Logger logger;
	final PluginManager pluginManager;

	public Engine(final BoshOptions options, final Logger logger) {
		this.logger = logger;
		this.pluginManager = new PluginManager(this);
		this.dispatcher = new Dispatcher(logger);
		this.globals = new HashMap<String, Object>();

		globals.put(DOMAIN, options.getDomain());

		this.bosh = new Bosh(options, dispatcher);
	}

	public Engine(final BoshOptions options, final LoggerOutput output) {
		this(options, new LoggerAdapter(output));
	}

	public void addListener(final PacketSubscriber listener) {
		dispatcher.addResponseListener(listener);
	}

	public Object getComponent(final String name) {
		return globals.get(name);
	}

	public String getGlobal(final String name) {
		return globals.get(name).toString();
	}

	public void publish(final Packet packet) {
		dispatcher.publish(packet);
	}

	public void register(final String name, final Object component) {
		globals.put(name, component);
	}

	public void send(final Packet stanza) {
		bosh.send(stanza.toString());
	}

	/**
	 * Set a name/value pair in a global store (session)
	 * 
	 * @param name
	 * @param value
	 */
	public void setGlobal(final String name, final String value) {
		logger.debug("Xmpp::setGlobal - '{0}' : '{1}'", name, value);
		globals.put(name, value);
	}

	public void start() {
		bosh.start();
	}

	public void stop() {

	}

}
