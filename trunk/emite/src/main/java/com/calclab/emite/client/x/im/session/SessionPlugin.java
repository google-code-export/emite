package com.calclab.emite.client.x.im.session;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.Globals;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.plugin.SenderPlugin;
import com.calclab.emite.client.plugin.dsl.PacketProducer;
import com.calclab.emite.client.x.core.ResourcePlugin;
import com.calclab.emite.client.x.core.SASLPlugin;

/**
 * @author dani
 */
public class SessionPlugin extends SenderPlugin {
    public static class Events {
        public static final Event ended = new Event("session:ended");
        public static final Event started = new Event("session:started");
    }

    public static Session getSession(final Components components) {
        return (Session) components.get("session");
    }

    final PacketProducer requestSession;
    final Action setAuthorizedState;
    final PacketProducer setStartedState;
    private final Session session;

    public SessionPlugin(final Dispatcher dispatcher, final Connection connection, final Globals globals) {
        super(connection);
        session = new Session(globals, dispatcher);

        requestSession = new PacketProducer() {
            public Packet logic(final Packet received) {
                final IQ iq = new IQ("requestSession", IQ.Type.set).From(globals.getJID()).To(globals.getDomain());
                iq.Include("session", "urn:ietf:params:xml:ns:xmpp-session");
                return iq;
            }
        };

        setAuthorizedState = new Action() {
            public void handle(final Packet received) {
                session.setState(Session.State.authorized);
            }
        };

        setStartedState = new PacketProducer() {
            public Packet logic(final Packet received) {
                session.setState(Session.State.connected);
                return Events.started;
            }
        };
    }

    @Override
    public void attach() {

        when.Event(Session.Events.login).Send(Connection.Events.start);

        when.Event(SASLPlugin.Events.authorized).Do(setAuthorizedState);

        when.Event(ResourcePlugin.Events.binded).Send(requestSession);

        when.IQ("requestSession").Publish(setStartedState);

    }

    @Override
    public void install() {
        register("session", session);
    }
}
