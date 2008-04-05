package com.calclab.examplechat.client.chatuiplugin.users;

import java.util.HashMap;
import java.util.Map;


public class GroupChatUserListPresenter implements GroupChatUserList {
    private GroupChatUserListPanel view;
    private final Map<String, GroupChatUser> users;

    public GroupChatUserListPresenter() {
        users = new HashMap<String, GroupChatUser>();
    }

    public void init(final GroupChatUserListPanel view) {
        this.view = view;
    }

    public void add(final GroupChatUser user) {
        users.put(user.getAlias(), user);
        view.addUser(user);
    }

    public GroupChatUser get(final String userAlias) {
        return users.get(userAlias);
    }

    public void remove(final GroupChatUser user) {
        view.remove(user);
    }

    public GroupChatUserListView getView() {
        return view;
    }

}
