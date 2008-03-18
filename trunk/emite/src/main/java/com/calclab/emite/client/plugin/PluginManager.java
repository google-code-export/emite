package com.calclab.emite.client.plugin;

import com.calclab.emite.client.IContainer;

/**
 * @author dani
 */
public class PluginManager implements IPluginManager {

	private final IContainer container;

	public PluginManager(final IContainer container) {
		this.container = container;
	}

	public void install(final String name, final Plugin2 plugin) {
		final FilterBuilder when = null;
		plugin.start(when, container);
	}

	public void uninstall(final String name) {
		throw new RuntimeException("not implemented");
	}
}
