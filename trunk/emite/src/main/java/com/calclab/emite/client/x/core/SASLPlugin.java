package com.calclab.emite.client.x.core;

import com.calclab.emite.client.Globals;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.packet.BasicPacket;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.plugin.SenderPlugin;
import com.calclab.emite.client.plugin.dsl.BussinessLogic;
import com.calclab.emite.client.utils.Base64Coder;

public class SASLPlugin extends SenderPlugin {
    public static class Events {
        public static final Event authorized = new Event("sasl:authorized");
    }

    private static final String SEP = new String(new char[] { 0 });

    final BussinessLogic authorization;

    final BussinessLogic restartAndAuthorize;

    public SASLPlugin(final Connection connection, final Globals globals) {
        super(connection);
        authorization = new BussinessLogic() {
            public Packet logic(final Packet cathced) {
                final Packet auth = createPlainAuthorization(globals);
                return auth;
            }

            protected String encode(final String domain, final String userName, final String password) {
                final String auth = userName + "@" + domain + SEP + userName + SEP + password;
                return Base64Coder.encodeString(auth);
            }

            private Packet createPlainAuthorization(final Globals globals) {
                final Packet auth = new BasicPacket("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism",
                        "PLAIN");
                final String encoded = encode(globals.getDomain(), globals.getUserName(), globals.getPassword());
                auth.addText(encoded);
                return auth;
            }
        };

        restartAndAuthorize = new BussinessLogic() {
            public Packet logic(final Packet received) {
                return Events.authorized;
            }

        };
    }

    @Override
    public void attach() {
        when.Packet("stream:features").Send(authorization);

        when.Packet("success", "xmlns", "urn:ietf:params:xml:ns:xmpp-sasl").Publish(restartAndAuthorize);
    }

    @Override
    public void install() {
    }
}
