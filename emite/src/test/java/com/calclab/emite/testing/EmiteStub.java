package com.calclab.emite.testing;

import static org.junit.Assert.*;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.DispatcherDefault;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.j2se.services.TigaseXMLService;

public class EmiteStub implements Emite {

    private IPacket lastPublished;
    private final DispatcherDefault dispatcher;
    private final TigaseXMLService xmler;
    private PacketListener lastCallback;
    private IPacket lastSentCallback;

    public EmiteStub() {
	xmler = new TigaseXMLService();
	dispatcher = new DispatcherDefault();
	lastPublished = null;
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

    public void publish(final IPacket packet) {
	lastPublished = packet;
    }

    public void send(final IPacket packet) {

    }

    public void send(final String category, final IPacket packet, final PacketListener listener) {
	this.lastSentCallback = packet;
	this.lastCallback = listener;
    }

    public void simulate(final IPacket packet) {
	dispatcher.publish(packet);
    }

    public void simulate(final String xml) {
	dispatcher.publish(xmler.toXML(xml));
    }

    public void subscribe(final Matcher matcher, final PacketListener packetListener) {
	dispatcher.subscribe(matcher, packetListener);
    }

    public void verifyPublished(final IPacket packet) {
	final IsPacketLike matcher = new IsPacketLike(packet);
	assertTrue(matcher.matches(lastPublished));
    }

    public PacketListener verifySendCallback(final IQ iq) {
	assertNotNull(lastSentCallback);
	final IsPacketLike matcher = new IsPacketLike(iq);
	assertTrue(matcher.matches(lastSentCallback));
	return lastCallback;
    }

}
