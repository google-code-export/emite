package com.calclab.emite.testing;

import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import com.calclab.emite.client.core.dispatcher.PacketListener;

public class PacketListenerTrap extends ArgumentMatcher<PacketListener> {
    private PacketListener listener;

    public PacketListener getListener() {
	return listener;
    }

    @Override
    public boolean matches(final Object argument) {
	this.listener = (PacketListener) argument;
	return true;
    }

    public PacketListener verify() {
	return Mockito.argThat(this);
    }

}
