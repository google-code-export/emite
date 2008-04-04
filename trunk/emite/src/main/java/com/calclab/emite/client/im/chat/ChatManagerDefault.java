package com.calclab.emite.client.im.chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.EmiteBosh;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.BasicPacket;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Message.MessageType;

public class ChatManagerDefault extends DispatcherComponent implements ChatManager {

    private final HashSet<ChatDefault> chats;
    private final Dispatcher dispatcher;
    private final Globals globals;
    private final ArrayList<ChatManagerListener> listeners;

    public ChatManagerDefault(final Dispatcher dispatcher, final Globals globals) {
	super(dispatcher);
	this.dispatcher = dispatcher;
	this.globals = globals;
	this.listeners = new ArrayList<ChatManagerListener>();
	this.chats = new HashSet<ChatDefault>();
    }

    public void addListener(final ChatManagerListener listener) {
	listeners.add(listener);

    }

    @Override
    public void attach() {
	when(new BasicPacket("message", null), new PacketListener() {
	    public void handle(final Packet received) {
		onReceived(new Message(received));
	    }
	});
    }

    public Collection<ChatDefault> getChats() {
	return chats;
    }

    public Chat newChat(final XmppURI xmppURI) {
	return createChat(xmppURI, null);
    }

    public void onReceived(final Message message) {
	final MessageType type = message.getType();
	switch (type) {
	case chat:
	case normal:
	    onChatMessageReceived(message);
	}
    }

    @Deprecated
    public void send(final String to, final String msg) {
	final Message message = new Message(globals.getOwnURI().toString(), to, msg);
	dispatcher.publish(new Event(EmiteBosh.Events.send).With(message));
    }

    void sendMessage(final Message message) {
	// FIXME
	dispatcher.publish(new Event(EmiteBosh.Events.send).With(message));
    }

    private ChatDefault createChat(final XmppURI from, final String thread) {
	final String theThread = thread != null ? thread : String.valueOf(Math.random() * 1000000);
	final ChatDefault chat = new ChatDefault(from, globals.getOwnURI(), theThread, this);
	chats.add(chat);
	for (final ChatManagerListener listener : listeners) {
	    listener.onChatCreated(chat);
	}
	return chat;
    }

    /**
     * We choose a chat by the thread, if no thread specified we look for the
     * same XmppURI, and if no resource specified we look for the same
     * node@domain
     * 
     * @param from
     * @param thread
     * @return
     */
    private ChatDefault findChat(final XmppURI from, final String thread) {
	ChatDefault selected = null;

	Log.debug("Finding chat for: " + from + ", " + thread);
	for (final ChatDefault chat : chats) {
	    if (thread != null) {
		if (thread.equals(chat.getThread())) {
		    return chat;
		}
	    } else {
		final XmppURI chatTargetURI = chat.getOtherURI();
		if (from.hasResource() && from.equals(chatTargetURI)) {
		    selected = chat;
		} else if (from.equalsNoResource(chatTargetURI)) {
		    selected = chat;
		}
	    }
	}

	return selected;
    }

    private void onChatMessageReceived(final Message message) {
	final XmppURI from = message.getFromURI();

	final String thread = message.getThread();

	ChatDefault chat = findChat(from, thread);
	if (chat == null) {
	    chat = createChat(from, thread);
	}
	chat.process(message);
    }

}
