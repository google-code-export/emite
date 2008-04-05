/*
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
package com.calclab.emite.client.core.services;

import com.calclab.emite.client.components.Container;

public class ServicesPlugin {

    private static final String COMPONENT_CONNECTOR = "connector";
    private static final String COMPONENT_SCHEDULER = "scheduler";
    private static final String COMPONENT_XML = "xmler";

    public static Connector getConnector(final Container container) {
	return (Connector) container.get(COMPONENT_CONNECTOR);
    }

    public static Scheduler getScheduler(final Container container) {
	return (Scheduler) container.get(COMPONENT_SCHEDULER);
    }

    public static XMLService getXMLService(final Container container) {
	return (XMLService) container.get(COMPONENT_XML);
    }

    public static void install(final Container container, final Connector connector, final XMLService xmler,
	    final Scheduler scheduler) {
	container.register(COMPONENT_CONNECTOR, connector);
	container.register(COMPONENT_XML, xmler);
	container.register(COMPONENT_SCHEDULER, scheduler);
    }

}
