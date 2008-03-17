package com.calclab.emite.client.plugin;

import com.calclab.emite.client.Engine;

public class PluginManager {
	private final Engine engine;

	public PluginManager(final Engine engine) {
		this.engine = engine;
	}

	public void install(final Plugin... modules) {
		for (final Plugin m : modules) {
			m.start(engine);
		}
	}

}
