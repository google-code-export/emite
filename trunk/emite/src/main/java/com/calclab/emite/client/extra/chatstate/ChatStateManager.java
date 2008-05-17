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
package com.calclab.emite.client.extra.chatstate;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import java.util.HashMap;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.xmpp.session.SessionComponent;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Message.Type;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html
 * 
 * This implementation is limited to chat conversations. Chat state in MUC rooms
 * are not supported to avoid multicast of occupant states (in a BOSH medium can
 * be a problem).
 * 
 */
public class ChatStateManager extends SessionComponent {

    private final HashMap<Chat, ChatState> chatStates;
    private final ChatManagerDefault chatManager;

    public ChatStateManager(final Emite emite, final ChatManagerDefault chatManager) {
        super(emite);
        this.chatManager = chatManager;
        chatStates = new HashMap<Chat, ChatState>();
        chatManager.addListener(new ChatManagerListener() {

            public void onChatClosed(final Chat chat) {
                final ChatState chatState = chatStates.get(chat);
                if (chatState != null && !chatState.getOtherState().equals(ChatState.Type.gone)) {
                    // We are closing, then we send the gone state
                    chatState.setOwnState(ChatState.Type.gone);
                }
                chatStates.remove(chat);
            }

            public void onChatCreated(final Chat chat) {
                chatStates.put(chat, new ChatState(chat, emite));
            }
        });
        install();
    }

    public ChatState getChatState(final Chat chat) {
        return chatStates.get(chat);
    }

    protected void eventMessage(final Message message) {
        final Type type = message.getType();
        if (type.equals(Type.chat)) {
            final XmppURI from = message.getFromURI();
            final String thread = message.getThread();

            Chat chat = chatManager.findChat(from, thread);
            if (chat != null) {
                ChatState chatState = chatStates.get(chat);
                if (chatState != null) {
                    // currently we ignore room chat states
                    chatState.fireMessageReceived(message);
                }
            }
        }
    }

    private void install() {
        emite.subscribe(when(new Packet("message", null)), new PacketListener() {
            public void handle(final IPacket received) {
                eventMessage(new Message(received));
            }
        });
    }

}
