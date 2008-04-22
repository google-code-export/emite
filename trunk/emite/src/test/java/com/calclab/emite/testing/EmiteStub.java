package com.calclab.emite.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    private IPacket lastSentCallback;
    private final ArrayList<IPacket> published;

    public EmiteStub() {
	xmler = new TigaseXMLService();
	dispatcher = new DispatcherDefault();
	published = new ArrayList<IPacket>();
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

    }

    public void send(final String category, final IPacket packet, final PacketListener listener) {
	this.lastSentCallback = packet;
	this.lastCallback = listener;
    }

    public void subscribe(final Matcher matcher, final PacketListener packetListener) {
	dispatcher.subscribe(matcher, packetListener);
    }

    public void verifyNotPublished(final Packet expected) {
	assertFalse(isPublished(expected));
    }

    public void verifyPublished(final IPacket expected) {
	assertTrue(isPublished(expected));
    }

    public void verifyPublishedTimes(final int times) {
	assertEquals(times, published.size());
    }

    public PacketListener verifySendCallback(final IPacket iq) {
	assertNotNull(lastSentCallback);
	final IsPacketLike matcher = new IsPacketLike(iq);
	assertTrue(matcher.matches(lastSentCallback));
	return lastCallback;
    }

    private boolean isPublished(final IPacket expected) {
	boolean isPublished = false;
	final IsPacketLike matcher = new IsPacketLike(expected);
	for (final IPacket packet : published) {
	    isPublished = isPublished ? isPublished : matcher.matches(packet);
	}
	return isPublished;
    }

}
