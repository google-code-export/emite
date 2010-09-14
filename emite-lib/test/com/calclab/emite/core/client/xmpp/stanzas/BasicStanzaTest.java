package com.calclab.emite.core.client.xmpp.stanzas;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.xmpp.datetime.XmppDateTime;

public class BasicStanzaTest {
    @Test
    public void shouldGiveDelay() {
	final BasicStanza stanza = new BasicStanza("name", "xmlns");
	XmppDateTime.useGWT21();

	XmppURI uri = uri("name@domain/resource");
	stanza.setTo(uri);
	assertEquals("name@domain/resource", stanza.getToAsString());
	stanza.setTo((XmppURI) null);
	IPacket delayNode = stanza.addChild("delay");
	delayNode.setAttribute("xmlns", "urn:xmpp:delay");
	delayNode.setAttribute("from", "name@domain/resource");
	delayNode.setAttribute("stamp", "1980-04-15T17:15:02.159+01:00");
	Delay delay = stanza.getDelay();
	assertNotNull(delay);
	Calendar cal = Calendar.getInstance();
	cal.clear();
	cal.set(1980, Calendar.APRIL, 15, 17, 15, 02);
	cal.setTimeZone(TimeZone.getTimeZone("GMT+01:00"));
	cal.set(Calendar.MILLISECOND, 159);
	Date date = cal.getTime();
	assertEquals(uri, delay.getFrom());
	assertEquals(date, delay.getStamp());
    }

    @Test
    public void shouldGiveDelayLegacyFormat() {
	final BasicStanza stanza = new BasicStanza("name", "xmlns");
	XmppDateTime.useGWT21();

	XmppURI uri = uri("name@domain/resource");
	stanza.setTo(uri);
	assertEquals("name@domain/resource", stanza.getToAsString());
	stanza.setTo((XmppURI) null);
	IPacket delayNode = stanza.addChild("x");
	delayNode.setAttribute("xmlns", "jabber:x:delay");
	delayNode.setAttribute("from", "name@domain/resource");
	delayNode.setAttribute("stamp", "19800415T17:15:02");
	Delay delay = stanza.getDelay();
	assertNotNull(delay);
	Calendar cal = Calendar.getInstance();
	cal.clear();
	cal.set(1980, Calendar.APRIL, 15, 17, 15, 02);
	Date date = cal.getTime();
	assertEquals(uri, delay.getFrom());
	assertEquals(date, delay.getStamp());
    }

    @Test
    public void shouldSetTextToChild() {
	final BasicStanza stanza = new BasicStanza("name", "xmlns");
	stanza.setTextToChild("child", "value");
	assertEquals("value", stanza.getFirstChild("child").getText());
	stanza.setTextToChild("child", null);
	assertSame(NoPacket.INSTANCE, stanza.getFirstChild("child"));
    }

    @Test
    public void shouldSetTo() {
	final BasicStanza stanza = new BasicStanza("name", "xmlns");

	stanza.setTo(uri("name@domain/resource"));
	assertEquals("name@domain/resource", stanza.getToAsString());
	stanza.setTo((XmppURI) null);
	assertNull(stanza.getToAsString());
    }
}
