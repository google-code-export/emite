package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.XMLService;

public class BoshResponder implements Responder {

	private final Dispatcher dispatcher;
	private Body request;
	private final XMLService xmler;

	public BoshResponder(final Dispatcher dispatcher, final XMLService xmler) {
		this.dispatcher = dispatcher;
		this.xmler = xmler;
	}

	public Packet bodyFromResponse(final String content) {
		return xmler.toXML(content);
	}

	public void clearBody() {
		request = null;
	}

	public Body getBody() {
		return request;
	}

	public String getResponse() {
		return null;
	}

	public void initBody(final long rid, final String sid) {
		if (this.request == null) {
			request = new Body(rid, sid);
		}
	}

}
