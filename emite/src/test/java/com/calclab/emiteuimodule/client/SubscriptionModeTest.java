package com.calclab.emiteuimodule.client;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SubscriptionModeTest {

    @Test
    public void musBeSerializable() {
	assertTrue(SubscriptionMode.autoAcceptAll instanceof IsSerializable);
    }
}
