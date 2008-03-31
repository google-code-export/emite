package com.calclab.emite.client.components;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;

public class DefaultContainer extends HashMap<String, Object> implements Container {
	private static final long serialVersionUID = 1L;

	public DefaultContainer() {
	}

	public Object get(final String componentName) {
		return super.get(componentName);
	}

	public void register(final String name, final Object component) {
		Log.debug("Registering component " + name);
		super.put(name, component);
	}
}
