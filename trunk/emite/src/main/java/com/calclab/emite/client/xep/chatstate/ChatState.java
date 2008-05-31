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
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.MessageInterceptor;
import com.calclab.emite.client.xmpp.stanzas.Message;

/**
 * XEP-0085: Chat State Notifications
 * http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 */
public class ChatState implements MessageInterceptor, ChatListener {
    public static enum NegotiationStatus {
        notStarted, started, rejected, accepted
    }

    public static enum Type {
        active, composing, pause, inactive, gone
    }

    public static final String XMLNS = "http://jabber.org/protocol/chatstates";

    private Type ownState;
    private Type otherState;
    private final Chat chat;
    private final ChatStateListenersCollection listeners;
    private NegotiationStatus negotiationStatus;

    public ChatState(final Chat chat) {
        this.chat = chat;
        negotiationStatus = NegotiationStatus.notStarted;
        listeners = new ChatStateListenersCollection();
    }

    public void addOtherStateListener(final ChatStateListener otherStateListener) {
        listeners.add(otherStateListener);
    }

    public NegotiationStatus getNegotiationStatus() {
        return negotiationStatus;
    }

    public Type getOtherState() {
        return otherState;
    }

    public Type getOwnState() {
        return ownState;
    }

    public void onBeforeReceive(final Message message) {
        // do nothing
    }

    public void onBeforeSend(final Message message) {
        switch (negotiationStatus) {
        case notStarted:
            negotiationStatus = NegotiationStatus.started;
        case accepted:
            boolean alreadyWithState = false;
            for (int i = 0; i < Type.values().length; i++) {
                if (message.hasChild(Type.values()[i].toString())) {
                    alreadyWithState = true;
                }
            }
            if (!alreadyWithState) {
                message.addChild(Type.active.toString(), XMLNS);
            }
            break;
        case rejected:
        case started:
            // do nothing
            break;
        }
    }

    public void onMessageReceived(final Chat chat, final Message message) {
        for (int i = 0; i < Type.values().length; i++) {
            final Type type = Type.values()[i];
            final String typeSt = type.toString();
            if (message.hasChild(typeSt) || message.hasChild("cha:" + typeSt)) {
                otherState = type;
                if (negotiationStatus.equals(NegotiationStatus.notStarted)) {
                    sendStateMessage(Type.active);
                }
                if (type.equals(Type.gone)) {
                    negotiationStatus = NegotiationStatus.notStarted;
                } else {
                    negotiationStatus = NegotiationStatus.accepted;
                }
                Log.info("Receiver other chat status: " + typeSt);
                fireOtherStateListeners(type);
            }
        }
    }

    public void onMessageSent(final Chat chat, final Message message) {
        // do nothing
    }

    public void setOwnState(final Type type) {
        // From XEP: a client MUST NOT send a second instance of any given
        // standalone notification (i.e., a standalone notification MUST be
        // followed by a different state, not repetition of the same state).
        // However, every content message SHOULD contain an <active/>
        // notification.
        if (negotiationStatus.equals(NegotiationStatus.accepted)) {
            if (ownState == null || !ownState.equals(type)) {
                this.ownState = type;
                Log.info("Setting own status to: " + type.toString());
                sendStateMessage(type);
            }
        }
    }

    private void fireOtherStateListeners(final Type type) {
        switch (type) {
        case active:
            listeners.onActive();
            break;
        case composing:
            listeners.onComposing();
            break;
        case pause:
            listeners.onPause();
            break;
        case inactive:
            listeners.onInactive();
            break;
        case gone:
            listeners.onGone();
            break;
        }
    }

    private void sendStateMessage(final Type type) {
        final Message message = new Message(chat.getFromURI(), chat.getOtherURI(), null).Thread(chat.getThread());
        message.addChild(type.toString(), XMLNS);
        chat.send(message);
    }
}
