package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;

public class ChatPlugin {
	private static final String COMPONENT_CHAT = "chat";

	public static ChatManager getChat(final Container container) {
		return (ChatManager) container.get(COMPONENT_CHAT);
	}

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final ChatManager chatManager = new ChatManager(dispatcher);
		container.install(COMPONENT_CHAT, chatManager);
	}

}
