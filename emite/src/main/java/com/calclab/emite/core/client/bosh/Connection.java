package com.calclab.emite.core.client.bosh;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.signal.Slot;

public interface Connection {

    public abstract void connect();

    public abstract void disconnect();

    public abstract boolean isConnected();

    public abstract void onError(final Slot<String> slot);

    public abstract void onStanzaReceived(final Slot<IPacket> slot);

    public abstract void onStanzaSent(final Slot<IPacket> slot);

    public abstract StreamSettings pause();

    public abstract void restartStream();

    public abstract boolean resume(StreamSettings settings);

    public abstract void send(final IPacket packet);

    public abstract void setSettings(Bosh3Settings settings);

}
