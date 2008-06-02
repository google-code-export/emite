/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.xep.chatstate;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.modular.client.signal.Slot;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 * 
 * This implementation is limited to chat conversations. Chat state in MUC rooms
 * are not supported to avoid multicast of occupant states (in a BOSH medium can
 * be a problem).
 * 
 */
public class ChatStateManager {

    public ChatStateManager(final ChatManager chatManager) {

	chatManager.onChatCreated(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		getChatState(chat);
	    }
	});

	chatManager.onChatClosed(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		Log.debug("Removing chat state to chat: " + chat.getID());
		final ChatState chatState = chat.getData(ChatState.class);
		if (chatState != null && chatState.getOtherState() != ChatState.Type.gone) {
		    // We are closing, then we send the gone state
		    chatState.setOwnState(ChatState.Type.gone);
		}
		chat.setData(ChatState.class, null);
	    }
	});
    }

    public ChatState getChatState(final Chat chat) {
	ChatState chatState = chat.getData(ChatState.class);
	if (chatState == null) {
	    chatState = createChatState(chat);
	}
	return chatState;
    }

    private ChatState createChatState(final Chat chat) {
	Log.debug("Adding chat state to chat: " + chat.getID());
	final ChatState chatState = new ChatState(chat);
	chat.setData(ChatState.class, chatState);
	chat.addMessageInterceptor(chatState);
	return chatState;
    }

}
