package com.calclab.emite.client.x.im;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.emite.client.plugin.SenderPlugin;
import com.calclab.emite.client.plugin.dsl.BussinessLogic;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class ChatPlugin extends SenderPlugin {

	public static Chat getChat(final Components container) {
		return (Chat) container.get("chat");
	}

	private final Chat chat;
	final BussinessLogic installListener;
	final BussinessLogic listenToIncomingMessages;

	public ChatPlugin(final Connection connection, final Dispatcher dispatcher) {
		super(connection);
		chat = new Chat(connection);

		listenToIncomingMessages = new BussinessLogic() {
			public Packet logic(final Packet received) {
				chat.onReceived(new Message(received));
				return null;
			}
		};

		installListener = new BussinessLogic() {
			public Packet logic(final Packet received) {
				// FIXME
				// dispatcher.addListener()
				// when.Message(globals.getJID()).call(listenToIncomingMessages);
				return null;
			}

		};
	}

	@Override
	public void attach() {
		when.Event(SessionPlugin.Events.started).Do(installListener);
		when.Event(SessionPlugin.Events.ended).Do(listenToIncomingMessages);
	}

	public void install(final Components components) {
		components.register("chat", chat);
	}
}
