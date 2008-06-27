package com.calclab.emite.client.core.bosh2;

import java.util.List;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class B2BoshManager {

    private boolean isFirstResponse;
    private final B2Stream stream;
    private final Signal<IPacket> onStanza;

    public B2BoshManager(final B2Bosh bosh, final B2Stream stream) {
	this.stream = stream;
	this.onStanza = new Signal<IPacket>("bosh:onStanza");

	bosh.onStart(new Slot<B2BoshOptions>() {
	    public void onEvent(final B2BoshOptions options) {
		isFirstResponse = true;
		stream.init(options.domain, options.version, options.wait, options.hold);
		bosh.send(stream.getBody());
	    }
	});

	bosh.onBody(new Slot<IPacket>() {
	    public void onEvent(final IPacket body) {
		if (isTerminal(body)) {

		} else {
		    if (isFirstResponse)
			handleFirstResponse(body);

		    stream.prepareToRespond();
		    try {
			fireStanzas(body);
			sendResponse();
		    } catch (final Exception e) {
			// bosh.error(new B2Error("", ""));
		    }
		}
	    }

	    private void fireStanzas(final IPacket body) {
		final List<? extends IPacket> children = body.getChildren();
		for (final IPacket stanza : children)
		    onStanza.fire(stanza);
	    }

	});
    }

    private void handleFirstResponse(final IPacket packet) {
	isFirstResponse = false;
	final String sid = packet.getAttribute("sid");
	final int poll = Integer.parseInt(packet.getAttribute("polling"));
	final int requests = Integer.parseInt(packet.getAttribute("requests"));
	stream.setAttributes(sid, poll, requests);
    }

    private boolean isTerminal(final IPacket body) {
	final String type = body.getAttribute("type");
	return "terminate".equals(type) || "terminal".equals(type);
    }

    private void sendResponse() {
	// TODO Auto-generated method stub

    }

}
