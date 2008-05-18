package com.calclab.emite.client.extra.chatstate;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.im.chat.BeforeSendMessageFormatter;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.stanzas.Message;

public class ChatState implements BeforeSendMessageFormatter {

    // TODO
    // Chat state negotiation :
    // 1. If the User desires chat state notifications, the initial content
    // message sent to the Contact MUST contain a chat state notification
    // extension, which SHOULD be <active/>.
    // 2. Until receiving a reply to the initial content message (or a
    // standalone notification) from the Contact, the User MUST NOT send
    // subsequent chat state notifications to the Contact.
    // 3. If the Contact replies to the initial content message but does not
    // include a chat state notification extension, the User MUST NOT send
    // subsequent chat state notifications to the Contact.
    // 4. If the Contact replies to the initial content message and includes an
    // <active/> notification (or sends a standalone notification to the User),
    // the User and Contact SHOULD send subsequent notifications for supported
    // chat states (as specified in the next subsection) by including an
    // <active/> notification in each content message and sending standalone
    // notifications for the chat states they support (at a minimum, the
    // <composing/> state).
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
    private final Emite emite;
    private ChatStateListenersCollection listeners;
    private NegotiationStatus negotiationStatus;

    public ChatState(final Chat chat, final Emite emite) {
        this.chat = chat;
        this.emite = emite;
        negotiationStatus = NegotiationStatus.notStarted;
    }

    public void addOtherStateListener(final ChatStateListener otherStateListener) {
        if (listeners == null) {
            listeners = new ChatStateListenersCollection();
        }
        listeners.add(otherStateListener);
    }

    public void fireMessageReceived(final Message message) {
        for (int i = 0; i < Type.values().length; i++) {
            final Type type = Type.values()[i];
            final String typeSt = type.toString();
            if (message.hasChild(typeSt) || message.hasChild("cha:" + typeSt)) {
                otherState = type;
                if (negotiationStatus.equals(NegotiationStatus.notStarted)) {
                    sendStateMessage(Type.active);
                }
                negotiationStatus = NegotiationStatus.accepted;
                Log.info("Receiver other chat status: " + typeSt);
                fireOtherStateListeners(type);
            }
        }
    }

    public void formatBeforeSend(final Message message) {
        switch (negotiationStatus) {
        case notStarted:
            negotiationStatus = NegotiationStatus.started;
        case accepted:
            message.add(Type.active.toString(), XMLNS);
            break;
        case rejected:
        case started:
            // do nothing
            break;
        }
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
        statePacket.add(type.toString(), XMLNS);
        emite.send(statePacket);
    }
}