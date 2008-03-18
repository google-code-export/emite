/**
 * 
 */
package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshListener;
import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.packet.Packet;

public class TestingConn implements IConnection {
	private final Logger logger;

	public TestingConn(final Logger logger) {
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
		logger.debug("CONN: send {0}", toBeSend);
	}

	public void start() {
		logger.debug("CONN - start");
	}

	public void stop() {
	}
}