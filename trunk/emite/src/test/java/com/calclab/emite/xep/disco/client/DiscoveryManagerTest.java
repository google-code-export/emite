package com.calclab.emite.xep.disco.client;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.testing.MockedSession;
import com.calclab.emite.xep.disco.client.DiscoveryManager;
import com.calclab.suco.testing.signal.MockSlot;

public class DiscoveryManagerTest {

    private DiscoveryManager manager;
    private MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession();
	manager = new DiscoveryManager(session);
    }

    @Test
    public void shouldInformListeners() {
	final MockSlot<DiscoveryManager> listener = new MockSlot<DiscoveryManager>();
	manager.onReady(listener);

    }
}
