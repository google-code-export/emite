package com.calclab.emite.j2se.services;

import com.calclab.emite.client.components.Component;
import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.ScheduledAction;
import com.calclab.emite.client.core.services.Services;

public class J2SEPlugin implements Component, Services {
    public static Container install(final Container container, final HttpConnectorListener listener) {
	container.register("services", new J2SEPlugin(listener));
	return container;
    }

    private final HttpConnector connector;
    private final ThreadScheduler scheduler;
    private final TigaseXMLService xmler;

    public J2SEPlugin(final HttpConnectorListener listener) {
	this.connector = new HttpConnector(listener);
	scheduler = new ThreadScheduler();
	xmler = new TigaseXMLService();
    }

    public long getCurrentTime() {
	return scheduler.getCurrentTime();
    }

    public void schedule(final int msecs, final ScheduledAction action) {
	scheduler.schedule(msecs, action);
    }

    public void send(final String httpBase, final String xml, final ConnectorCallback callback)
	    throws ConnectorException {
	connector.send(httpBase, xml, callback);
    }

    public String toString(final IPacket packet) {
	return xmler.toString(packet);
    }

    public IPacket toXML(final String xml) {
	return xmler.toXML(xml);
    }
}
