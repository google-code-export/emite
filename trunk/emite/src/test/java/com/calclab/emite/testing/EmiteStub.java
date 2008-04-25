package com.calclab.emite.testing;

import static org.junit.Assert.*;
import java.util.ArrayList;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.DispatcherDefault;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.j2se.services.TigaseXMLService;

public class EmiteStub implements Emite {

    private final DispatcherDefault dispatcher;
    private final TigaseXMLService xmler;
    private PacketListener lastCallback;
    private final ArrayList<IPacket> published;
    private IPacket lastIQSent;
    private final ArrayList<IPacket> sent;
    private StringBuffer contentsString;

    public EmiteStub() {
	xmler = new TigaseXMLService();
	dispatcher = new DispatcherDefault();
	published = new ArrayList<IPacket>();
	lastIQSent = null;
	sent = new ArrayList<IPacket>();
    }

    public void addListener(final DispatcherStateListener listener) {

    }

    public void answer(final IPacket iq) {
	lastCallback.handle(iq);
    }

    public void answer(final String iq) {
	lastCallback.handle(xmler.toXML(iq));
    }

    public void answerSuccess() {
	lastCallback.handle(new IQ(Type.result));
    }

    public void clearPublished() {
	published.clear();
    }

    public void publish(final IPacket packet) {
	published.add(packet);
    }

    public void receives(final IPacket packet) {
	dispatcher.publish(packet);
    }

    public void receives(final String xml) {
	dispatcher.publish(xmler.toXML(xml));
    }

    public void send(final IPacket packet) {
	sent.add(packet);
    }

    public void sendIQ(final String category, final IPacket packet, final PacketListener listener) {
	lastIQSent = packet;
	this.lastCallback = listener;
    }

    public void subscribe(final Matcher matcher, final PacketListener packetListener) {
	dispatcher.subscribe(matcher, packetListener);
    }

    public PacketListener verifyIQSent(final IPacket iq) {
	assertNotNull(lastIQSent);
	final String message = "Expected: " + iq + "but was: " + lastIQSent;
	assertTrue(message, new IsPacketLike(iq).matches(lastIQSent));
	return lastCallback;
    }

    public void verifyIQSent(final String xml) {
	verifyIQSent(xmler.toXML(xml));
    }

    public void verifyNotPublished(final Packet packet) {
	assertNotContains(packet, published);
    }

    public void verifyPublished(final IPacket expected) {
	assertContains(expected, published);
    }

    public void verifyPublished(final String xml) {
	verifyPublished(xmler.toXML(xml));
    }

    public void verifyPublishedTimes(final int times) {
	assertEquals(times, published.size());
    }

    public void verifySent(final IPacket packet) {
	assertContains(packet, sent);
    }

    public void verifySent(final String expected) {
	final IPacket packet = xmler.toXML(expected);
	verifySent(packet);
    }

    private void assertContains(final IPacket expected, final ArrayList<IPacket> list) {
	final StringBuffer buffer = new StringBuffer();
	final boolean isContained = contains(expected, list, buffer);
	assertTrue("Expected " + expected + " contained in " + buffer, isContained);
    }

    private void assertNotContains(final Packet expected, final ArrayList<IPacket> list) {
	final StringBuffer buffer = new StringBuffer();
	final boolean isContained = contains(expected, list, buffer);
	assertFalse("Expected " + expected + " contained in\n" + buffer, isContained);
    }

    private boolean contains(final IPacket expected, final ArrayList<IPacket> list, final StringBuffer buffer) {
	boolean isContained = false;
	final IsPacketLike matcher = new IsPacketLike(expected);
	for (final IPacket packet : list) {
	    buffer.append("[").append(packet.toString()).append("]");
	    isContained = isContained ? isContained : matcher.matches(packet);
	}
	return isContained;
    }

}
