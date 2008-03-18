package com.calclab.emite.client;

import java.util.HashMap;

import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.log.Logger;

public class Container extends HashMap<String, Object> implements IContainer {
	private static final String CONNECTION = "connection";
	private static final String DISPATCHER = "dispatcher";
	private static final String GLOBALS = "globals";
	private static final String LOGGER = "logger";
	private static final long serialVersionUID = 1L;

	public Object get(final String componentName) {
		return super.get(componentName);
	}

	public IConnection getConnection() {
		return (IConnection) get(CONNECTION);
	}

	public IDispatcher getDispatcher() {
		return (IDispatcher) get(DISPATCHER);
	}

	public IGlobals getGlobals() {
		return (IGlobals) get(GLOBALS);
	}

	public void register(final String name, final Object component) {
		super.put(name, component);
	}

	public void setConnection(final IConnection bosh) {
		register(CONNECTION, bosh);
	}

	public void setDispatcher(final IDispatcher dispatcher) {
		register(DISPATCHER, dispatcher);
	}

	public void setGlobals(final IGlobals globals) {
		register(GLOBALS, globals);
	}

	public void setLogger(final Logger logger) {
		register(LOGGER, logger);
	}
}
