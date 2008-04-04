/**
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
