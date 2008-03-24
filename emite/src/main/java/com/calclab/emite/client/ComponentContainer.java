package com.calclab.emite.client;

import java.util.HashMap;

import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.plugin.PluginManager;

public class ComponentContainer extends HashMap<String, Object> implements Components {
	private static final long serialVersionUID = 1L;
	private final Logger logger;

	public ComponentContainer(final Logger logger) {
		this.logger = logger;
	}

	public Object get(final String componentName) {
		return super.get(componentName);
	}

	public Connection getConnection() {
		return (Connection) get(Components.CONNECTION);
	}

	public Dispatcher getDispatcher() {
		return (Dispatcher) get(Components.DISPATCHER);
	}

	public Globals getGlobals() {
		return (Globals) get(Components.GLOBALS);
	}

	public Logger getLogger() {
		return logger;
	}

	public PluginManager getPluginManager() {
		return (PluginManager) get(Components.PLUGIN_MANAGER);
	}

	public void register(final String name, final Object component) {
		logger.log(Logger.DEBUG, "Registering component '{0}'", name);
		super.put(name, component);
	}

	public void setConnection(final Connection bosh) {
		register(Components.CONNECTION, bosh);
	}

	public void setDispatcher(final Dispatcher dispatcher) {
		register(Components.DISPATCHER, dispatcher);
	}

	public void setGlobals(final Globals globals) {
		register(Components.GLOBALS, globals);
	}

	public void setPluginManager(final PluginManager pluginManager) {
		register(Components.PLUGIN_MANAGER, pluginManager);

	}
}
