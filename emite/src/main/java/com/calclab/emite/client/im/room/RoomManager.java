package com.calclab.emite.client.im.room;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.MessageType;

public class RoomManager extends EmiteComponent {

	public RoomManager(final Emite emite) {
		super(emite);
	}

	@Override
	public void attach() {
		when("message", new PacketListener() {
			public void handle(final Packet received) {
				onMessageReceived(new Message(received));
			}
		});
	}

	protected void onMessageReceived(final Message message) {
		if (message.getType() == MessageType.groupchat) {

		}
	}

}
