package com.calclab.examplechat.client.chatuiplugin.users;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;

public class GroupChatUserListPanel extends Panel implements GroupChatUserListView {
    private final UserGrid userGrid;

    public GroupChatUserListPanel() {
        userGrid = new UserGrid();
        super.add(userGrid);
        super.setLayout(new FitLayout());
        // super.setBorder(false);
    }

    public void addUser(final GroupChatUser user) {
        userGrid.addUser(user);
    }

    public void remove(final GroupChatUser user) {
        userGrid.addUser(user);
    }
}
