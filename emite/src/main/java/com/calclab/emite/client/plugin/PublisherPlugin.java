package com.calclab.emite.client.plugin;

import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.plugin.dsl.FilterBuilder;

public abstract class PublisherPlugin implements Plugin {

	protected FilterBuilder when;

	public abstract void attach();

	public void attach(final Dispatcher dispatcher) {
		this.when = new FilterBuilder(dispatcher, null);
	}

}
