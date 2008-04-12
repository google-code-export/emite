package com.calclab.examplechat.client.chatuiplugin.room;

import org.ourproject.kune.platf.client.View;

import com.calclab.examplechat.client.chatuiplugin.users.RoomUserUI;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenuItemList;

public interface RoomUserListUIView extends View {

    void addUser(RoomUserUI roomUser, UserGridMenuItemList menu);

    void removeAllUsers();

    void removeUser(RoomUserUI roomUser);

    void updateUser(RoomUserUI roomUser, UserGridMenuItemList menuItemList);
}
