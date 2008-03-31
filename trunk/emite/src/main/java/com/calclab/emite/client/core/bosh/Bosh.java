package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;

public interface Bosh {

	public static class Events {
		public static final Event error = new Event("connection:has:error");
		public static final Event restart = new Event("connection:do:restart");
		public static final Event send = new Event("connection:do:send");
		public static final Event start = new Event("connection:do:start");
		public static final Event stop = new Event("connection:do:stop");;
	}

	public void send(Packet toBeSend);
}
