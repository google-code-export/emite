package com.calclab.examplechat.client.chatuiplugin.room;

import java.util.Collection;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.extra.muc.RoomUser;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUIListener;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUIPresenter;

public class RoomUIPresenter extends ChatUIPresenter implements RoomUI {

    private RoomUIView view;

    private String subject;

    public RoomUIPresenter(final ChatUIListener listener) {
        super(listener);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String newSubject) {
        subject = newSubject;
    }

    public void setUsers(final Collection<RoomUser> users) {
        // TODO Auto-generated method stub

    }

    public void init(final RoomUIView view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

}
