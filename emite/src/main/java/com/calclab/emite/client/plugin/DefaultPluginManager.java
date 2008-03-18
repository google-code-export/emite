package com.calclab.emite.client.plugin;

import java.util.ArrayList;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.plugin.dsl.FilterBuilder;

/**
 * @author dani
 */
public class DefaultPluginManager implements PluginManager {

	private final Components container;
	private final ArrayList<Plugin> installed;
	private final Logger logger;

	public DefaultPluginManager(final Logger logger, final Components container) {
		this.logger = logger;
		this.container = container;
		installed = new ArrayList<Plugin>();
	}

	public void install(final String name, final Plugin plugin) {
		logger.debug("Installing plugin {0}", name);
		plugin.install(container);
		installed.add(plugin);
	}

	public void start() {
		final FilterBuilder when = new FilterBuilder(container);
		for (final Plugin p : installed) {
			p.start(when);
		}
	}

	public void uninstall(final String name) {
		throw new RuntimeException("not implemented");
	}
}
