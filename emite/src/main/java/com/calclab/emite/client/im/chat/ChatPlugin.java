package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.ContainerPlugin;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;

public class ChatPlugin {
	private static final String COMPONENT_CHAT = "chat";

	public static ChatManagerDefault getChat(final Container container) {
		return (ChatManagerDefault) container.get(COMPONENT_CHAT);
	}

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Globals globals = ContainerPlugin.getGlobals(container);
		final ChatManagerDefault chatManagerDefault = new ChatManagerDefault(dispatcher, globals);
		container.install(COMPONENT_CHAT, chatManagerDefault);
	}

}
