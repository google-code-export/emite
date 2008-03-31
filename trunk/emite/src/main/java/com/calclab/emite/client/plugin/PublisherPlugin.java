package com.calclab.emite.client.plugin;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.plugin.dsl.FilterBuilder;

public abstract class PublisherPlugin implements Plugin {

	protected FilterBuilder when;

	private Container container;

	public abstract void attach();

	public void attach(final Dispatcher dispatcher) {
		this.when = new FilterBuilder(dispatcher, null);
		attach();
	}

	public abstract void install();

	public void install(final Container container) {
		this.container = container;
		install();
		this.container = null;
	}

	protected Object getComponent(final String componentName) {
		final Object component = container.get(componentName);
		if (component == null) {
			throw new RuntimeException("Unsatisfied depency to: " + componentName);
		}
		return component;
	}

	protected void register(final String name, final Object component) {
		container.register(name, component);
	}

}
