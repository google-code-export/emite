package com.calclab.emite.client.core.services;

import com.calclab.emite.client.components.Container;

public class ServicesPlugin {

	private static final String COMPONENT_CONNECTOR = "connector";
	private static final String COMPONENT_GLOBALS = "globals";
	private static final String COMPONENT_SCHEDULER = "scheduler";
	private static final String COMPONENT_XML = "xmler";

	public static Connector getConnector(final Container container) {
		return (Connector) container.get(COMPONENT_CONNECTOR);
	}

	public static Globals getGlobals(final Container container) {
		return (Globals) container.get(COMPONENT_GLOBALS);
	}

	public static Scheduler getScheduler(final Container container) {
		return (Scheduler) container.get(COMPONENT_SCHEDULER);
	}

	public static XMLService getXMLService(final Container container) {
		return (XMLService) container.get(COMPONENT_XML);
	}

	public static void install(final Container container, final Connector connector, final XMLService xmler,
			final Scheduler scheduler) {
		container.register(COMPONENT_GLOBALS, new HashGlobals());
		container.register(COMPONENT_CONNECTOR, connector);
		container.register(COMPONENT_XML, xmler);
		container.register(COMPONENT_SCHEDULER, scheduler);
	}

}
