package com.calclab.emite.client.components;

import java.util.ArrayList;
import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;

@SuppressWarnings("serial")
public class DefaultContainer extends HashMap<String, Object> implements Container {
	private final ArrayList<Startable> startables;

	public DefaultContainer() {
		this.startables = new ArrayList<Startable>();
	}

	public Object get(final String componentName) {
		return super.get(componentName);
	}

	public void install(final String name, final Startable startable) {
		register(name, startable);
		startables.add(startable);
	}

	public void register(final String name, final Object component) {
		Log.debug("Registering component " + name);
		super.put(name, component);
	}

	public void start() {
		for (final Startable c : startables) {
			c.start();
		}
	}

	public void stop() {
		for (final Startable c : startables) {
			c.stop();
		}
	}
}
