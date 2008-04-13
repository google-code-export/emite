package com.calclab.examplechat.client.chatuiplugin.room;

import java.util.Collection;
import java.util.Iterator;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;

import com.calclab.emite.client.extra.muc.RoomUser;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.AbstractPresenter;
import com.calclab.examplechat.client.chatuiplugin.EmiteUiPlugin;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUIListener;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUIPresenter;
import com.calclab.examplechat.client.chatuiplugin.users.RoomUserUI;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenuItem;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenuItemList;
import com.calclab.examplechat.client.chatuiplugin.users.RoomUserUI.RoomUserType;
import com.calclab.examplechat.client.chatuiplugin.utils.XmppJID;

public class RoomUIPresenter extends ChatUIPresenter implements RoomUI, AbstractPresenter {

    private RoomUIView view;

    private String subject;

    private RoomUserListUIPanel roomUserListUI;

    public RoomUIPresenter(final ChatUIListener listener) {
        super(listener);
    }

    public void doAction(final String eventName, final Object param) {
        DefaultDispatcher.getInstance().fire(eventName, param);
    }

    public String getSubject() {
        return subject;
    }

    public View getUserListView() {
        return roomUserListUI.getView();
    }

    public View getView() {
        return view;
    }

    public void init(final RoomUIView view, final RoomUserListUIPanel roomUserListUI) {
        super.init(view);
        this.view = view;
        this.roomUserListUI = roomUserListUI;
    }

    public void setSubject(final String newSubject) {
        subject = newSubject;
    }

    public void setUsers(final Collection<RoomUser> users) {
        for (Iterator<RoomUser> iterator = users.iterator(); iterator.hasNext();) {
            RoomUser roomUser = iterator.next();
            XmppURI userUri = roomUser.getUri();
            // FIXME real user alias
            String userAlias = userUri.getNode();
            RoomUserUI roomUserUI = new RoomUserUI(new XmppJID(userUri), userAlias, super.getColor(userAlias),
                    RoomUserType.participant);
            roomUserListUI.addUser(roomUserUI, createUserMenu(roomUserUI));
        }
    }

    private UserGridMenuItem<Object> createNoActionsMenuItem() {
        return new UserGridMenuItem<Object>("", "", EmiteUiPlugin.NO_ACTION, null);
    }

    private UserGridMenuItemList createUserMenu(final RoomUserUI roomUserUI) {
        final UserGridMenuItemList itemList = new UserGridMenuItemList();
        switch (roomUserUI.getUserType()) {
        default:
            itemList.addItem(createNoActionsMenuItem());
        }
        return itemList;
    }
}
