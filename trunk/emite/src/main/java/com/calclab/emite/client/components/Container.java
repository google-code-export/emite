package com.calclab.emite.client.components;

public interface Container {

	Object get(String componentName);

	void register(String componentName, Object component);

}
