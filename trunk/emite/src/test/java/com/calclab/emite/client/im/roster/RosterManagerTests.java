package com.calclab.emite.client.im.roster;

import static org.mockito.Mockito.*;
import static com.calclab.emite.client.TestMatchers.*;
import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;

public class RosterManagerTests {
    private RosterManager manager;
    private Roster roster;
    private Emite emite;

    @Before
    public void aaCreate() {
	emite = mock(Emite.class);
	roster = mock(Roster.class);
	manager = new RosterManager(emite, roster);
    }

    @Test
    public void shouldAddItemsToRoster() {
	final BasicStanza query = new BasicStanza("query", "jabber:iq:roster");
	query.add("item", null).With("jid", "name1@domain").With("name", "complete name1").With("subscription", "both");
	query.add("item", null).With("jid", "name2@domain").With("name", "complete name2").With("subscription", "both");
	manager.eventRoster(new IQ(Type.result).With(query));
	verify(roster).setItems(isListOfSize(2));
    }

    @Test
    public void shouldRequestRosterOnLogin() {
	manager.eventLoggedIn();
	verify(emite).send(anyString(), isPacket(new IQ(IQ.Type.get).WithQuery("jabber:iq:roster", null)),
		(PacketListener) anyObject());
    }
}
