package com.calclab.emite.client.plugin;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.log.Logger;

/**
 * @author dani
 */
public class DefaultPluginManager implements PluginManager {

	private final Components container;
	private final Logger logger;
	private final FilterBuilder when;

	public DefaultPluginManager(final Logger logger, final Components container) {
		this.logger = logger;
		this.container = container;
		when = new FilterBuilder(container.getDispatcher(), container.getConnection());
	}

	public void install(final String name, final Plugin plugin) {
		logger.debug("Installing plugin {0}", name);
		plugin.start(when, container);
	}

	public void uninstall(final String name) {
		throw new RuntimeException("not implemented");
	}
}
