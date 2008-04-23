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
    private IPacket sentByCallback;
    private final ArrayList<IPacket> sent;

    public EmiteStub() {
	xmler = new TigaseXMLService();
	dispatcher = new DispatcherDefault();
	published = new ArrayList<IPacket>();
	sentByCallback = null;
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

    public void send(final String category, final IPacket packet, final PacketListener listener) {
	sentByCallback = packet;
	this.lastCallback = listener;
    }

    public void subscribe(final Matcher matcher, final PacketListener packetListener) {
	dispatcher.subscribe(matcher, packetListener);
    }

    public void verifyNotPublished(final Packet packet) {
	assertFalse(contains(packet, published));
    }

    public void verifyPublished(final IPacket expected) {
	assertTrue(contains(expected, published));
    }

    public void verifyPublishedTimes(final int times) {
	assertEquals(times, published.size());
    }

    public PacketListener verifySendCallback(final IPacket iq) {
	assertNotNull(sentByCallback);
	assertTrue(new IsPacketLike(iq).matches(sentByCallback));
	return lastCallback;
    }

    public void verifySendCallback(final String xml) {
	verifySendCallback(xmler.toXML(xml));
    }

    public void verifySent(final String expected) {
	assertTrue(contains(xmler.toXML(expected), sent));
    }

    private boolean contains(final IPacket expected, final ArrayList<IPacket> list) {
	boolean isContained = false;
	final IsPacketLike matcher = new IsPacketLike(expected);
	for (final IPacket packet : list) {
	    isContained = isContained ? isContained : matcher.matches(packet);
	}
	return isContained;
    }

}
