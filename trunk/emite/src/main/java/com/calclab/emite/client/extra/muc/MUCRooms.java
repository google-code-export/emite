package com.calclab.emite.client.extra.muc;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;

public class MUCRooms extends EmiteComponent implements Rooms {
    private final Globals globals;

    public MUCRooms(final Emite emite, final Globals globals) {
	super(emite);
	this.globals = globals;
    }

    @Override
    public void attach() {
	when(SessionManager.Events.loggedIn, new PacketListener() {
	    public void handle(final Packet received) {
		sendRoomsQuery();
	    }
	});
    }

    /**
     * @see http://www.xmpp.org/extensions/xep-0045.html#disco-rooms
     */
    protected void sendRoomsQuery() {
	final IQ iq = new IQ("rooms_1", Type.get).From(globals.getOwnURI()).To(globals.getDomain());
	iq.setQuery("http://jabber.org/protocol/disco#items");
	emite.send(iq);
    }
}
