package com.calclab.emite.client.xep.disco;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.TestingListener;

public class DiscoveryManagerTest {

    private EmiteTestHelper emite;
    private DiscoveryManager manager;

    @Before
    public void beforeTests() {
	emite = new EmiteTestHelper();
	manager = new DiscoveryManager(emite);
    }

    @Test
    public void shouldInformListeners() {
	final TestingListener<DiscoveryManager> listener = new TestingListener<DiscoveryManager>();
	manager.onReady(listener);

    }
}
