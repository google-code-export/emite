package com.calclab.emite.client.extra.muc;

import java.util.Collection;

public interface RoomListener {
    void onUserChanged(Collection<RoomUser> users);
}
