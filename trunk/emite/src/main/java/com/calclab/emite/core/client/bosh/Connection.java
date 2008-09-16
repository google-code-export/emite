package com.calclab.emite.core.client.bosh;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.listener.Listener;

public interface Connection {

    public abstract void connect();

    public abstract void disconnect();

    public abstract boolean isConnected();

    public abstract void onError(final Listener<String> listener);

    public abstract void onStanzaReceived(final Listener<IPacket> listener);

    public abstract void onStanzaSent(final Listener<IPacket> listener);

    public abstract StreamSettings pause();

    public abstract void removeOnStanzaReceived(Listener<IPacket> listener);

    public abstract void restartStream();

    public abstract boolean resume(StreamSettings settings);

    public abstract void send(final IPacket packet);

    public abstract void setSettings(Bosh3Settings settings);

}
