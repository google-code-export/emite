package com.calclab.emite.client.extra.chatstate;

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

    // VICENTE: por qué estos atributos no son privados!? alguna razón de peso
    // para no hacerlo a través de get/set?
    Type ownState;
    Type otherState;
    private final Chat chat;
    private final Emite emite;
    private final ChatStateListenersCollection listeners;

    public ChatState(final Chat chat, final Emite emite) {
	this.chat = chat;
	this.emite = emite;
	addMessageListener(chat);
	this.listeners = new ChatStateListenersCollection();
    }

    // VICENTE: no es obvio que el listener se refiere al estado del otro?
    public void addOtherStateListener(final ChatStateListener otherStateListener) {
	listeners.add(otherStateListener);
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
	    final Packet statePacket = new Packet("message");
	    statePacket.setAttribute("from", chat.getFromURI().toString());
	    statePacket.setAttribute("to", chat.getOtherURI().toString());
	    statePacket.setAttribute("type", "chat");
	    final String thread = chat.getThread();
	    if (thread != null) {
		final Packet threadPacket = new Packet("thread");
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
		    final Type type = Type.values()[i];
		    if (message.hasAttribute(type.toString())) {
			otherState = type;
			Log.info("Receiver other chat status: " + type.toString());
			fireOtherStateListeners(type);
		    }
		}
	    }

	    public void onMessageSent(final Chat chat, final Message message) {
	    }

	});
    }

    // VICENTE: este método debe pertenecer a ChatState y no a
    // ChatState$ChatListener (que es donde estaba)
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
}
