package com.calclab.emite.client.x.core;

import com.calclab.emite.client.Globals;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.plugin.SenderPlugin;
import com.calclab.emite.client.plugin.dsl.PacketProducer;

public class ResourcePlugin extends SenderPlugin {

    public static class Events {
        public static final Event binded = new Event("resource:binded");
    }

    final PacketProducer requestResourceBinding;
    final PacketProducer resourceBinded;

    public ResourcePlugin(final Connection connection, final Globals globals) {
        super(connection);
        requestResourceBinding = new PacketProducer() {
            public Packet logic(final Packet cathced) {
                final IQ iq = new IQ("bindRequest", IQ.Type.set);
                iq.add("bind", "urn:ietf:params:xml:ns:xmpp-bind").add("resource", null).addText("mensa");
                return iq;
            }
        };

        resourceBinded = new PacketProducer() {
            public Packet logic(final Packet iq) {
                final String jid = iq.getFirstChildren("bind").getFirstChildren("jid").getText();
                globals.setJID(jid);
                return Events.binded;
            }
        };
    }

    @Override
    public void attach() {
        when.Event(SASLPlugin.Events.authorized).Send(requestResourceBinding);
        when.IQ("bindRequest").Publish(resourceBinded);
    }

    @Override
    public void install() {
    }

}
