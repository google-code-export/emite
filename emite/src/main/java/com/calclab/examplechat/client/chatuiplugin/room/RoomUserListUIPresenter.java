package com.calclab.examplechat.client.chatuiplugin.room;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;

import com.calclab.examplechat.client.chatuiplugin.AbstractPresenter;
import com.calclab.examplechat.client.chatuiplugin.users.RoomUserUI;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenuItemList;

public class RoomUserListUIPresenter implements RoomUserListUI, AbstractPresenter {

    private RoomUserListUIView view;

    public RoomUserListUIPresenter() {
    }

    public void addUser(final RoomUserUI roomUser, final UserGridMenuItemList menuItemList) {
        addUser(roomUser, menuItemList);
    }

    public void doAction(final String action, final Object value) {
        DefaultDispatcher.getInstance().fire(action, value);
    }

    public View getView() {
        return view;
    }

    public void init(final RoomUserListUIView view) {
        this.view = view;
    }

    public void removeAllUsers() {
        view.removeAllUsers();
    }

    public void removeUser(final RoomUserUI roomUser) {
        view.removeUser(roomUser);

    }

    public void updateUser(final RoomUserUI roomUser, final UserGridMenuItemList menuItemList) {
        view.updateUser(roomUser, menuItemList);
    }

}
