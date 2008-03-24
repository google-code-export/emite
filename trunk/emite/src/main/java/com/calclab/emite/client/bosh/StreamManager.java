package com.calclab.emite.client.bosh;

import java.util.List;

import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Packet;

public class StreamManager {

    public final Action clear;
    public final Action publishStanzas;
    private String sid;

    public StreamManager(final Bosh bosh, final Dispatcher dispatcher) {
        this.sid = null;

        clear = new Action() {
            public void handle(final Packet stanza) {
                sid = null;
            }
        };

        publishStanzas = new Action() {
            public void handle(final Packet received) {
                if (sid == null) {
                    sid = received.getAttribute("sid");
                }
                bosh.setSID(sid);
                final List<? extends Packet> children = received.getChildren();
                for (final Packet stanza : children) {
                    dispatcher.publish(stanza);
                }
            }
        };

    }
}
