package com.calclab.emite.client;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Connector;
import com.calclab.emite.client.core.services.Scheduler;
import com.calclab.emite.client.core.services.ServicesPlugin;
import com.calclab.emite.client.core.services.XMLService;
import com.calclab.emite.client.im.chat.ChatPlugin;
import com.calclab.emite.client.im.presence.PresencePlugin;
import com.calclab.emite.client.im.room.RoomPlugin;
import com.calclab.emite.client.im.roster.RosterPlugin;
import com.calclab.emite.client.xmpp.resource.ResourceBindingPlugin;
import com.calclab.emite.client.xmpp.sasl.SASLPlugin;
import com.calclab.emite.client.xmpp.session.SessionPlugin;

public class Plugins {

	public static void installDefaultPlugins(final Container container, final XMLService xmlService,
			final Connector connector, final Scheduler scheduler, final BoshOptions options) {

		installCorePlugins(container, xmlService, connector, scheduler, options);
		installXMPPPlugins(container);
		installIMPlugins(container);
	}

	private static void installCorePlugins(final Container container, final XMLService xmlService,
			final Connector connector, final Scheduler scheduler, final BoshOptions options) {
		DispatcherPlugin.install(container);
		ServicesPlugin.install(container, connector, xmlService, scheduler);
		BoshPlugin.install(container, options);
	}

	private static void installIMPlugins(final Container container) {
		// Instant Messaging
		ChatPlugin.install(container);
		SessionPlugin.install(container);
		RosterPlugin.install(container);
		PresencePlugin.install(container);
		RoomPlugin.install(container);
	}

	private static void installXMPPPlugins(final Container container) {
		SASLPlugin.install(container);
		ResourceBindingPlugin.install(container);
	}

}
