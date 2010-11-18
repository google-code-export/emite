/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.widgets.client.habla;

import java.util.HashMap;

import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.widgets.client.chat.ChatWidget;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.ioc.Provider;

public class ConversationsController {
    private final ChatManager manager;
    private final HashMap<String, ChatWidget> chats;
    private final Provider<ChatWidget> chatWidgetFactory;

    public ConversationsController(final ChatManager manager, final Provider<ChatWidget> chatWidgetFactory) {
	this.manager = manager;
	this.chatWidgetFactory = chatWidgetFactory;
	this.chats = new HashMap<String, ChatWidget>();
    }

    public void setWidget(final ConversationsWidget widget) {
	manager.onChatCreated(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		ChatWidget chatWidget = chats.get(chat.getID());
		if (chatWidget == null) {
		    chatWidget = chatWidgetFactory.get();
		    chatWidget.getController().setChat(chat);
		    chats.put(chat.getID(), chatWidget);
		    widget.add(chat.getURI().toString(), chatWidget);
		}
	    }
	});
    }

}
