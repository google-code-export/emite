package com.calclab.examplechat.client.chatuiplugin.room;

import java.util.Collection;

import com.calclab.emite.client.extra.muc.RoomUser;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUI;

public interface RoomUI extends ChatUI {

    void setSubject(String newSubject);

    String getSubject();

    void setUsers(Collection<RoomUser> users);

}
