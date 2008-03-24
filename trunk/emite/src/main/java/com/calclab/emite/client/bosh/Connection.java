package com.calclab.emite.client.bosh;

import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;

public interface Connection {

    public static class Events {
        public static Event start = new Event("connection:start");
        public static Event error = new Event("connection:error");
    }

    public void send(Packet toBeSend);
}
