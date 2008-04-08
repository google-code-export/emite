package com.calclab.emite.client.extra.muc;

import java.util.Collection;

public interface RoomManagerListener {
    void onRoomsChanged(Collection<Room> rooms);
}
