package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshListener;
import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.packet.Packet;

public class TestHelper {

	public static class FakeConnection implements IConnection {
		private final Logger logger;

		public FakeConnection(final Logger logger) {
			this.logger = logger;
		}

		public void addListener(final BoshListener listener) {
		}

		public void pause() {
		}

		public void removeListener(final BoshListener listener) {
		}

		public void resume() {
		}

		public void send(final Packet toBeSend) {
		}

		public void start() {
			logger.debug("FCON - start");
		}

		public void stop() {
		}
	}

	public static Xmpp createXMPP() {
		final Components cs = XMPPPlugin.createComponents(new LoggerOutput() {
			public void log(final int level, final String message) {
				System.out.println("[" + level + "]: " + message);
			}
		});

		cs.setConnection(new FakeConnection(cs.getLogger()));

		XMPPPlugin.installPlugins(cs);

		return new Xmpp(cs);
	}
}
