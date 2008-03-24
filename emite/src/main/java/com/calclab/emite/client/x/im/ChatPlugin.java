package com.calclab.emite.client.x.im;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.emite.client.plugin.SenderPlugin;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class ChatPlugin extends SenderPlugin {

    public static Chat getChat(final Components container) {
        return (Chat) container.get("chat");
    }

    final Action installListener;
    final Action listenToIncomingMessages;
    private final Chat chat;

    public ChatPlugin(final Connection connection, final Dispatcher dispatcher) {
        super(connection);
        chat = new Chat(connection);

        listenToIncomingMessages = new Action() {
            public void handle(final Packet received) {
                chat.onReceived(new Message(received));

            }
        };

        installListener = new Action() {
            public void handle(final Packet stanza) {
                // FIXME
                // dispatcher.addListener()
                // when.Message(globals.getJID()).call(listenToIncomingMessages);
            }
        };

    }

    @Override
    public void attach() {
        when.Event(SessionPlugin.Events.started).Do(installListener);
        when.Event(SessionPlugin.Events.ended).Do(listenToIncomingMessages);
    }

    @Override
    public void install() {
        register("chat", chat);
    }

}
