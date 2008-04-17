package com.calclab.emite.testing;

import java.util.ArrayList;

import junit.framework.Assert;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.IPacket;

public class InstallationTester implements Emite {

    public static interface InstallTest {
	void prepare(Emite emite, InstallVerifier verifier);
    }

    public static interface InstallVerifier {
	void shouldAttachTo(IPacket packet);
    }

    private final ArrayList<Matcher> matchers;

    public InstallationTester(final InstallTest verifier) {
	this.matchers = new ArrayList<Matcher>();
	verifier.prepare(this, new InstallVerifier() {
	    public void shouldAttachTo(final IPacket packet) {
		assertCatched(packet);
	    }

	});
    }

    public void addListener(final DispatcherStateListener listener) {
    }

    public void publish(final IPacket packet) {
    }

    public void send(final IPacket packet) {
    }

    public void send(final String category, final IPacket packet, final PacketListener listener) {
    }

    public void subscribe(final Matcher matcher, final PacketListener packetListener) {
	matchers.add(matcher);
    }

    private void assertCatched(final IPacket packet) {
	boolean isCatched = false;
	for (final Matcher m : matchers) {
	    if (m.matches(packet)) {
		isCatched = true;
	    }
	}
	Assert.assertTrue("Should catch : " + packet, isCatched);
    }
}
