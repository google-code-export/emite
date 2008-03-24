package com.calclab.emite.client.bosh;

import java.util.List;

import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.plugin.dsl.BussinessLogic;

public class StreamManager {

    public final BussinessLogic clear;
    public final BussinessLogic publishStanzas;
    private String sid;

    public StreamManager(final Bosh bosh, final Dispatcher dispatcher) {
        this.sid = null;

        clear = new BussinessLogic() {
            public Packet logic(final Packet received) {
                sid = null;
                return null;
            }
        };

        publishStanzas = new BussinessLogic() {
            public Packet logic(final Packet received) {
                if (sid == null) {
                    sid = received.getAttribute("sid");
                }
                bosh.setSID(sid);
                final List<? extends Packet> children = received.getChildren();
                for (final Packet stanza : children) {
                    dispatcher.publish(stanza);
                }
                return null;
            }
        };

    }
}
