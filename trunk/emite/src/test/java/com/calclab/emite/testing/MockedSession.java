package com.calclab.emite.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import com.calclab.emite.client.core.bosh3.Bosh3Settings;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.session.AbstractSession;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.j2se.services.TigaseXMLService;
import com.calclab.suco.client.signal.Slot;

public class MockedSession extends AbstractSession {
    private XmppURI currentUser;
    private Session.State state;
    private final TigaseXMLService xmler;
    private final ArrayList<IPacket> sent;
    private IPacket lastIQSent;
    private Slot<IPacket> lastIQSlot;

    public MockedSession() {
	this((XmppURI) null);
    }

    public MockedSession(final String user) {
	this(XmppURI.uri(user));
    }

    public MockedSession(final XmppURI user) {
	xmler = new TigaseXMLService();
	sent = new ArrayList<IPacket>();
	if (user != null) {
	    this.currentUser = user;
	}
    }

    public void answer(final IPacket iq) {
	lastIQSlot.onEvent(iq);
    }

    public void answer(final String iq) {
	answer(xmler.toXML(iq));
    }

    public void answerSuccess() {
	answer(new IQ(Type.result));
    }

    public XmppURI getCurrentUser() {
	return currentUser;
    }

    public Session.State getState() {
	return state;
    }

    public boolean isLoggedIn() {
	return currentUser != null;
    }

    public void login(final XmppURI uri, final String password, final Bosh3Settings settings) {
    }

    public void logout() {
	onLoggedOut.fire(currentUser);
    }

    public void receives(final Message message) {
	onMessage.fire(message);
    }

    public void receives(final Presence presence) {
	onPresence.fire(presence);
    }

    public void receives(final String received) {
	final IPacket stanza = xmler.toXML(received);
	final String name = stanza.getName();
	if (name.equals("message")) {
	    onMessage.fire(new Message(stanza));
	} else if (name.equals("presence")) {
	    onPresence.fire(new Presence(stanza));
	} else {
	    throw new RuntimeException("Not valid received: " + received);
	}

    }

    public void send(final IPacket packet) {
	sent.add(packet);
    }

    public void sendIQ(final String id, final IQ iq, final Slot<IPacket> slot) {
	this.lastIQSent = iq;
	this.lastIQSlot = slot;
    }

    public void setCurrentUser(final XmppURI currentUser) {
	this.currentUser = currentUser;
    }

    public void setLoggedIn(final String uri) {
	setLoggedIn(XmppURI.uri(uri));
    }

    public void setLoggedIn(final XmppURI userURI) {
	this.currentUser = userURI;
	onLoggedIn.fire(userURI);
    }

    public void setState(final Session.State state) {
	this.state = state;
    }

    public Slot<IPacket> verifyIQSent(final IPacket iq) {
	assertNotNull(lastIQSent);
	EmiteAsserts.assertPacketLike(iq, lastIQSent);
	return lastIQSlot;
    }

    public void verifyIQSent(final String xml) {
	verifyIQSent(xmler.toXML(xml));
    }

    public void verifyNotSent(final IPacket packet) {
	assertNotContains(packet, sent);
    }

    public void verifyNotSent(final String xml) {
	verifyNotSent(xmler.toXML(xml));
    }

    public void verifySent(final IPacket packet) {
	assertContains(packet, sent);
    }

    public void verifySent(final String expected) {
	final IPacket packet = xmler.toXML(expected);
	verifySent(packet);
    }

    public void verifySentNothing() {
	assertEquals("number of sent stanzas", 0, sent.size());
    }

    private void assertContains(final IPacket expected, final ArrayList<IPacket> list) {
	final StringBuffer buffer = new StringBuffer();
	final boolean isContained = contains(expected, list, buffer);
	assertTrue("Expected " + expected + " contained in " + buffer, isContained);
    }

    private void assertNotContains(final IPacket expected, final ArrayList<IPacket> list) {
	final StringBuffer buffer = new StringBuffer();
	final boolean isContained = contains(expected, list, buffer);
	assertFalse("Expected " + expected + " contained in\n" + buffer, isContained);
    }

    private boolean contains(final IPacket expected, final ArrayList<IPacket> list, final StringBuffer buffer) {
	boolean isContained = false;
	final IsPacketLike matcher = new IsPacketLike(expected);
	for (final IPacket packet : list) {
	    buffer.append("[").append(packet.toString()).append("]");
	    isContained = isContained ? isContained : matcher.matches(packet, System.out);
	}
	return isContained;
    }
}
