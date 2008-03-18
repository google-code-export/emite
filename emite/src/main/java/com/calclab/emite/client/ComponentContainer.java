package com.calclab.emite.client;

import java.util.HashMap;

import com.calclab.emite.client.action.Dispatcher;
import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.plugin.PluginManager;

public class ComponentContainer extends HashMap<String, Object> implements Components {
	private static final String CONNECTION = "connection";
	private static final String DISPATCHER = "dispatcher";
	private static final String GLOBALS = "globals";
	private static final String PLUGIN_MANAGER = "pluginManager";
	private static final long serialVersionUID = 1L;
	private final Logger logger;

	public ComponentContainer(final Logger logger) {
		this.logger = logger;
	}

	public Object get(final String componentName) {
		return super.get(componentName);
	}

	public IConnection getConnection() {
		return (IConnection) get(CONNECTION);
	}

	public Dispatcher getDispatcher() {
		return (Dispatcher) get(DISPATCHER);
	}

	public Globals getGlobals() {
		return (Globals) get(GLOBALS);
	}

	public Logger getLogger() {
		return logger;
	}

	public PluginManager getPluginManager() {
		return (PluginManager) get(PLUGIN_MANAGER);
	}

	public void register(final String name, final Object component) {
		logger.log(Logger.DEBUG, "Registering component '{0}'", name);
		super.put(name, component);
	}

	public void setConnection(final IConnection bosh) {
		register(CONNECTION, bosh);
	}

	public void setDispatcher(final Dispatcher dispatcher) {
		register(DISPATCHER, dispatcher);
	}

	public void setGlobals(final Globals globals) {
		register(GLOBALS, globals);
	}

	public void setPluginManager(final PluginManager pluginManager) {
		register(PLUGIN_MANAGER, pluginManager);

	}
}
