package com.calclab.emite.client.extra.muc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class RoomOccupantTest {

    @Test
    public void should() {
	final Occupant occupant = new Occupant(XmppURI.parse("valid@uri"), "not valid affiliation", "not valid role");
	assertEquals(Occupant.Affiliation.none, occupant.getAffiliation());
	assertEquals(Occupant.Role.unknown, occupant.getRole());
    }
}
