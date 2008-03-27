package com.calclab.emite.client.x.im.chat;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.emite.client.plugin.SenderPlugin;

public class ChatPlugin extends SenderPlugin {

	public static Chat getChat(final Components container) {
		return (Chat) container.get("chat");
	}

	private final Chat chat;

	public ChatPlugin(final Dispatcher dispatcher, final Connection connection) {
		super(connection);
		chat = new Chat(dispatcher);
	}

	@Override
	public void attach() {
		when.Packet("message").Do(new Action() {
			public void handle(final Packet received) {
				chat.onReceived(new Message(received));
			}
		});
	}

	@Override
	public void install() {
		register("chat", chat);
	}

}
