package com.calclab.emite.client.extra.chatstate;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.xmpp.stanzas.Message;

public class ChatState {

    public static enum Type {
        active, composing, pause, inactive, gone
    }

    Type ownState;
    Type otherState;
    private final Chat chat;
    private final Emite emite;
    private final ArrayList<ChatStateListener> otherStateListeners;

    public ChatState(final Chat chat, final Emite emite) {
        this.chat = chat;
        this.emite = emite;
        addMessageListener(chat);
        this.otherStateListeners = new ArrayList<ChatStateListener>();
    }

    public void addOtherStateListener(final ChatStateListener otherStateListener) {
        otherStateListeners.add(otherStateListener);
    }

    public Type getOtherState() {
        return otherState;
    }

    public Type getOwnState() {
        return ownState;
    }

    public void setOwnState(final Type type) {
        // From XEP: a client MUST NOT send a second instance of any given
        // standalone notification (i.e., a standalone notification MUST be
        // followed by a different state, not repetition of the same state).
        // However, every content message SHOULD contain an <active/>
        // notification.
        if (ownState == null || !ownState.equals(type)) {
            this.ownState = type;
            Log.info("Setting own status to: " + type.toString());
            Packet statePacket = new Packet("message");
            statePacket.setAttribute("from", chat.getFromURI().toString());
            statePacket.setAttribute("to", chat.getOtherURI().toString());
            statePacket.setAttribute("type", "chat");
            String thread = chat.getThread();
            if (thread != null) {
                Packet threadPacket = new Packet("thread");
                threadPacket.setText(thread);
                statePacket.addChild(threadPacket);
            }
            statePacket.add(type.toString(), "http://jabber.org/protocol/chatstates");
            emite.send(statePacket);
        }
    }

    private void addMessageListener(final Chat chat) {
        chat.addListener(new ChatListener() {
            public void onMessageReceived(final Chat chat, final Message message) {
                for (int i = 0; i < Type.values().length; i++) {
                    Type type = Type.values()[i];
                    if (message.hasAttribute(type.toString())) {
                        otherState = type;
                        Log.info("Receiver other chat status: " + type.toString());
                        fireOtherStateListeners(type);
                    }
                }
            }

            public void onMessageSent(final Chat chat, final Message message) {
            }

            private void fireOtherStateListeners(final Type type) {
                for (int i = 0; i < otherStateListeners.size(); i++) {
                    switch (type) {
                    case active:
                        otherStateListeners.get(i).onActive();
                        break;
                    case composing:
                        otherStateListeners.get(i).onComposing();
                        break;
                    case pause:
                        otherStateListeners.get(i).onPause();
                        break;
                    case inactive:
                        otherStateListeners.get(i).onInactive();
                        break;
                    case gone:
                        otherStateListeners.get(i).onGone();
                        break;
                    }
                }
            }
        });
    }
}
