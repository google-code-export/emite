package com.calclab.emite.client.plugin;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Components;
import com.calclab.emite.client.dispatcher.Dispatcher;

/**
 * @author dani
 */
public class DefaultPluginManager implements PluginManager {

	private final Components container;
	private final ArrayList<Plugin> installed;

	public DefaultPluginManager(final Components container) {
		this.container = container;
		installed = new ArrayList<Plugin>();
	}

	public void install(final String name, final Plugin plugin) {
		Log.debug("Installing plugin " + name);
		plugin.install(container);
		installed.add(plugin);
	}

	public void start() {
		final Dispatcher dis = container.getDispatcher();
		for (final Plugin p : installed) {
			p.attach(dis);
		}
	}

	public void uninstall(final String name) {
		throw new RuntimeException("not implemented");
	}
}
