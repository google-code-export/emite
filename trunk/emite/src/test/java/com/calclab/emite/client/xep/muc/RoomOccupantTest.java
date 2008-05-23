package com.calclab.emite.client.xep.muc;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.client.xep.muc.Occupant;

public class RoomOccupantTest {

    @Test
    public void should() {
	final Occupant occupant = new Occupant(uri("valid@uri"), "not valid affiliation", "not valid role");
	assertEquals(Occupant.Affiliation.none, occupant.getAffiliation());
	assertEquals(Occupant.Role.unknown, occupant.getRole());
    }
}
