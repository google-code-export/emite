package com.calclab.emite.client.bosh;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.packet.XMLService;
import com.calclab.emite.client.plugin.PublisherPlugin;
import com.calclab.emite.client.scheduler.Scheduler;
import com.calclab.emite.client.x.core.SASLPlugin;

public class BoshPlugin extends PublisherPlugin {
	private Bosh bosh;
	private final Connector connector;
	private final BoshOptions options;
	private final XMLService xmler;
	private final Scheduler scheduler;

	public BoshPlugin(final Connector connector, final XMLService xmler,
			final Scheduler scheduler, final BoshOptions options) {
		this.connector = connector;
		this.xmler = xmler;
		this.scheduler = scheduler;
		this.options = options;
	}

	@Override
	public void attach() {
		when.Event(SASLPlugin.Events.authorized).Do(bosh.restartStream);
		when.Event(Connection.Events.start).Do(bosh.sendCreation);
		when.Event(Connection.Events.error).Do(bosh.stop);
		when.Event(Connection.Events.send).Do(bosh.send);
		when.Packet("body").Do(bosh.publishStanzas);
	}

	@Override
	public void install() {
		final Dispatcher dispatcher = getDispatcher();
		bosh = new Bosh(dispatcher, getGlobals(), connector, xmler, scheduler,
				options);

		register(Components.CONNECTION, bosh);
		dispatcher.addListener(new DispatcherStateListener() {
			public void afterDispatching() {
				bosh.firePackets();
			}

			public void beforeDispatching() {
				bosh.catchPackets();
			}
		});

	}

}
