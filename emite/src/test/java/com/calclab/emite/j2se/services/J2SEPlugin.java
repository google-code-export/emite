package com.calclab.emite.j2se.services;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.ScheduledAction;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.client.core.services.ServicesModule;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;

public class J2SEPlugin extends ServicesModule implements Services, Module {
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

    public void onLoad(final Container container) {
	ServicesModule.setServices(container, this);
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
