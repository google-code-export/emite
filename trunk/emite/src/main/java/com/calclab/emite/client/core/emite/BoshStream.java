package com.calclab.emite.client.core.emite;

import com.calclab.emite.client.core.bosh.Stream;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;

public class BoshStream implements Stream {
    private Packet body;
    private long requestID;

    public BoshStream() {
    }

    public void addResponse(final IPacket child) {
	body.addChild(child);
    }

    public Packet clearBody() {
	final Packet result = body;
	body = null;
	return result;
    }

    public boolean isEmpty() {
	return body.getChildrenCount() == 0;
    }

    public void newRequest(final String sid) {
	if (this.body == null) {
	    requestID++;
	    body = createBody();
	    setSID(sid);
	} else {
	}
    }

    public void setRestart(final String domain) {
	body.With("xmpp:restart", "true").With("xmlns:xmpp", "urn:xmpp:xbosh").With("xml:lang", "en")
		.With("to", domain);
    }

    public void setSID(final String sid) {
	if (sid != null) {
	    body.setAttribute("sid", sid);
	}
    }

    public void setTerminate() {
	body.setAttribute("type", "terminate");
    }

    public void start(final String domain) {
	this.requestID = (int) (Math.random() * 1245234);
	body = createInitialBody(domain);
    }

    private Packet createBody() {
	final Packet initalBody = new Packet("body", "http://jabber.org/protocol/httpbind");
	initalBody.setAttribute("rid", String.valueOf(requestID));
	return initalBody;
    }

    private Packet createInitialBody(final String domain) {
	final Packet initalBody = createBody();
	initalBody.With("content", "text/xml; charset=utf-8").With("to", domain).With("secure", "true").With("ver",
		"1.6").With("wait", "60").With("ack", "1").With("hold", "1").With("xml:lang", "en");
	return initalBody;
    }
}
