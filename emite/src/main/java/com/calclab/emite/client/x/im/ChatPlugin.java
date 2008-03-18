package com.calclab.emite.client.x.im;

import com.calclab.emite.client.IContainer;
import com.calclab.emite.client.IDispatcher;
import com.calclab.emite.client.action.BussinessLogic;
import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.emite.client.plugin.FilterBuilder;
import com.calclab.emite.client.plugin.Plugin2;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class ChatPlugin implements Plugin2 {

	public static Chat getChat(final IContainer container) {
		return (Chat) container.get("chat");
	}

	private final Chat chat;
	final BussinessLogic installListener;
	final BussinessLogic listenToIncomingMessages;

	public ChatPlugin(final IConnection connection, final IDispatcher dispatcher) {
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

	public void start(final FilterBuilder when, final IContainer components) {
		components.register("chat", chat);

		when.Event(SessionPlugin.Events.started).Do(installListener);

		when.Event(SessionPlugin.Events.ended).Do(listenToIncomingMessages);
	}

}
