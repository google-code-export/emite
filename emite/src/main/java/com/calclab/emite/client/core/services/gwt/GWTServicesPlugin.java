package com.calclab.emite.client.core.services.gwt;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.ScheduledAction;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class GWTServicesPlugin implements Services {

    public static void install(final Container container) {
	ServicesPlugin.install(container, new GWTServicesPlugin());
    }

    public long getCurrentTime() {
	return GWTScheduler.getCurrentTime();
    }

    public void schedule(final int msecs, final ScheduledAction action) {
	GWTScheduler.schedule(msecs, action);
    }

    public void send(final String httpBase, final String request, final ConnectorCallback callback)
	    throws ConnectorException {
	GWTConnector.send(httpBase, request, callback);
    }

    public String toString(final IPacket packet) {
	return GWTXMLService.toString(packet);
    }

    public IPacket toXML(final String xml) {
	return GWTXMLService.toXML(xml);
    }

}
