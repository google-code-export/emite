package com.calclab.emite.client.bosh;

import com.calclab.emite.client.packet.Packet;

public interface IConnection {

	public void addListener(BoshListener listener);

	public void pause();

	public void removeListener(BoshListener listener);

	public void resume();

	public void send(Packet toBeSend);

	public void start();

	public void stop();
}
