package com.calclab.emite.client.x.im;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Dispatcher;
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
	}

	@Override
	public void install() {
		register("chat", chat);
	}

}
