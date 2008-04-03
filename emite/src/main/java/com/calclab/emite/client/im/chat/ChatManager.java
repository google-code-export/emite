package com.calclab.emite.client.im.chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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

public class ChatManager extends DispatcherComponent {

    private final HashMap<String, Chat> chats;
    private final Dispatcher dispatcher;
    private final Globals globals;
    private final ArrayList<ChatManagerListener> listeners;

    public ChatManager(final Dispatcher dispatcher, final Globals globals) {
	super(dispatcher);
	this.dispatcher = dispatcher;
	this.globals = globals;
	this.listeners = new ArrayList<ChatManagerListener>();
	this.chats = new HashMap<String, Chat>();
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

    public Collection<Chat> getChats() {
	return chats.values();
    }

    public Chat newChat(final XmppURI xmppURI) {
	final String thread = String.valueOf(Math.random() * 1000000);
	return createChat(xmppURI, thread);
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

    private Chat createChat(final XmppURI from, final String thread) {
	final Chat chat = new Chat(from, globals.getOwnURI(), thread, this);
	chats.put(from.toString() + thread, chat);
	for (final ChatManagerListener listener : listeners) {
	    listener.onChatCreated(chat);
	}
	return chat;
    }

    private Chat findChat(final XmppURI from, final String thread) {
	return chats.get(from.toString() + thread);
    }

    private void onChatMessageReceived(final Message message) {
	final XmppURI from = message.getFromURI();

	String thread = message.getThread();
	thread = thread != null ? thread : "";

	Chat chat = findChat(from, thread);
	if (chat == null) {
	    chat = createChat(from, thread);
	}
	chat.process(message);
    }

    void sendMessage(final Message message) {
	dispatcher.publish(new Event(EmiteBosh.Events.send).With(message));
    }

}
