package com.calclab.emite.client.extra.muc;

import java.util.Collection;

import com.calclab.emite.client.im.chat.ChatListener;

public interface RoomListener extends ChatListener {
    void onUserChanged(Collection<RoomUser> users);
}
