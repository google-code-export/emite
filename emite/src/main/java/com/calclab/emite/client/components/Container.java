package com.calclab.emite.client.components;


public interface Container {

	Object get(String componentName);

	void reg(String componentName, Object component);

	void register(String componentName, Startable startable);

	void start();

	void stop();
}
