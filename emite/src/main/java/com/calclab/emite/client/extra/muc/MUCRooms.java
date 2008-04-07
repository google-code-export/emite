package com.calclab.emite.client.extra.muc;

import java.util.ArrayList;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.client.xmpp.stanzas.Message.MessageType;

public class MUCRooms extends EmiteComponent implements Rooms {
    private final Globals globals;
    private final ArrayList<RoomsListener> listeners;
    private final ArrayList<Room> rooms;

    public MUCRooms(final Emite emite, final Globals globals) {
	super(emite);
	this.globals = globals;
	this.listeners = new ArrayList<RoomsListener>();
	this.rooms = new ArrayList<Room>();
    }

    public void addListener(final RoomsListener listener) {
	listeners.add(listener);
    }

    @Override
    public void attach() {
	when(SessionManager.Events.loggedIn, new PacketListener() {
	    public void handle(final Packet received) {
		sendRoomsQuery();
	    }
	});
	when("message", new PacketListener() {
	    public void handle(final Packet received) {
		onMessageReceived(new Message(received));
	    }
	});
    }

    protected void onMessageReceived(final Message message) {
	if (message.getType() == MessageType.groupchat) {
	    // mensaje a una habitación
	    // Room room = this.getRoom(message.getFrom());
	    // room.dispatch(message);
	}
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
