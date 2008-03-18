package com.calclab.emite.client.bosh;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.Globals;
import com.calclab.emite.client.action.BussinessLogic;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.plugin.dsl.FilterBuilder;
import com.calclab.emite.client.x.core.SASLPlugin;

public class BoshPlugin implements Plugin {
	private final Bosh bosh;
	final BussinessLogic restartStream;

	public BoshPlugin(final Connector connector, final BoshOptions options, final Logger logger, final Globals globals) {
		this.bosh = new Bosh(connector, options, logger);

		restartStream = new BussinessLogic() {
			public Packet logic(final Packet received) {
				bosh.setRestart(true);
				return null;
			}
		};

		globals.setDomain(options.getDomain());
	}

	public void install(final Components components) {
		components.setConnection(bosh);
	}

	public void start(final FilterBuilder when) {
		when.Event(SASLPlugin.Events.authorized).Do(restartStream);
	}

}
