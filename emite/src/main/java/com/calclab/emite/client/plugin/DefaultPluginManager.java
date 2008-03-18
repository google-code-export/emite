package com.calclab.emite.client.plugin;

import com.calclab.emite.client.Components;

/**
 * @author dani
 */
public class DefaultPluginManager implements PluginManager {

	private final Components container;
	private final FilterBuilder when;

	public DefaultPluginManager(final Components container) {
		this.container = container;
		when = new FilterBuilder(container.getDispatcher(), container.getConnection());
	}

	public void install(final String name, final Plugin plugin) {
		plugin.start(when, container);
	}

	public void uninstall(final String name) {
		throw new RuntimeException("not implemented");
	}
}
