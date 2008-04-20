package com.calclab.emite.client.im.roster;

import static com.calclab.emite.testing.TestMatchers.isListOfSize;
import static com.calclab.emite.testing.TestMatchers.packetLike;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.PacketListenerTrap;

public class RosterManagerTest {
    private Emite emite;
    private RosterManager manager;
    private Roster roster;

    @Before
    public void aaCreate() {
	emite = mock(Emite.class);
	roster = mock(Roster.class);
	manager = new RosterManager(emite, roster);
    }

    @Test
    public void shouldAddItemsToRoster() {
	final BasicStanza query = new BasicStanza("query", "jabber:iq:roster");
	query.add("item", null).With("jid", "name1@	domain").With("name", "complete name1")
		.With("subscription", "both");
	query.add("item", null).With("jid", "name2@domain").With("name", "complete name2").With("subscription", "both");
	manager.eventRoster(new IQ(Type.result).With(query));
	verify(roster).setItems(isListOfSize(2));
    }

    @Test
    public void shouldRemoveItemsToRoster() {
	final XmppURI uri = XmppURI.parse("name@domain/res");
	manager.requestRemoveItem(uri);
	final PacketListenerTrap trap = new PacketListenerTrap();
	verify(emite).send(anyString(), (IPacket) anyObject(), trap.verify());
	trap.getListener().handle(new IQ(Type.result));
	verify(roster).removeItem(uri);
    }

    @Test
    public void shouldRequestRosterOnLogin() {
	manager.eventLoggedIn();
	verify(emite).send(anyString(), packetLike(new IQ(IQ.Type.get).WithQuery("jabber:iq:roster", null)),
		(PacketListener) anyObject());
    }
}
