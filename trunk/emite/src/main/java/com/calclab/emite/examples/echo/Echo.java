package com.calclab.emite.examples.echo;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.matcher.Matchers;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.Message;

public class Echo {

    private final Emite emite;

    public Echo(final Emite emite) {
	this.emite = emite;
	install();
    }

    protected void install() {
	emite.subscribe(Matchers.when("message"), new PacketListener() {
	    public void handle(final IPacket received) {
		echo(new Message(received));
	    }
	});
    }

    private void echo(final Message message) {
	// exchange the from and to...
	final Message response = new Message(message.getToURI(), message.getFromURI(), message.getBody());
	emite.send(response);
    }
}
