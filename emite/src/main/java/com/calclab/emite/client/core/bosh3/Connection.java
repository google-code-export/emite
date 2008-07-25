package com.calclab.emite.client.core.bosh3;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.suco.client.signal.Slot;

public interface Connection {

    public abstract void connect();

    public abstract void disconnect();

    public abstract boolean isConnected();

    public abstract void onError(final Slot<String> slot);

    public abstract void onStanzaReceived(final Slot<IPacket> slot);

    public abstract void restartStream();

    public abstract void send(final IPacket packet);

    public abstract void setSettings(Bosh3Settings settings);

}
