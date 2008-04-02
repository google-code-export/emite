package com.calclab.emite.client.components;

public interface Container extends Startable {

	Object get(String componentName);

	void install(String componentName, Startable startable);

	void register(String componentName, Object component);

}
