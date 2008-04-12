package com.calclab.examplechat.client.chatuiplugin.room;

import java.util.Collection;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.extra.muc.RoomUser;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUI;

public interface RoomUI extends ChatUI {

    String getSubject();

    View getUserListView();

    void setSubject(String newSubject);

    void setUsers(Collection<RoomUser> users);

}
