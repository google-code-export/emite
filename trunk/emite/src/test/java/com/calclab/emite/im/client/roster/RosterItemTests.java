package com.calclab.emite.im.client.roster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.j2se.services.TigaseXMLService;
import com.calclab.emite.testing.EmiteAsserts;
import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.*;

public class RosterItemTests {

    @Test
    public void shouldConvertToStanza() {
	final RosterItem item = new RosterItem(uri("name@domain/RESOURCE"), null, "TheName");
	item.addGroup("group1");
	item.addGroup("group2");
	EmiteAsserts.assertPacketLike("<item jid='name@domain' name='TheName'>"
		+ "<group>group1</group><group>group2</group></item>", item.addStanzaTo(new Packet("all")));
    }

    @Test
    public void shouldParseStanza() {
	final RosterItem item = RosterItem.parse(p("<item jid='romeo@example.net' name='R' subscription='both'>"
		+ "<group>Friends</group><group>X</group></item>"));
	assertEquals("R", item.getName());
	assertEquals(RosterItem.Subscription.both, item.getSubscription());
	assertEquals(2, item.getGroups().size());
	assertTrue(item.getGroups().contains("Friends"));
	assertTrue(item.getGroups().contains("X"));
    }

    private IPacket p(final String xml) {
	final IPacket packet = TigaseXMLService.getSingleton().toXML(xml);
	return packet;
    }
}
