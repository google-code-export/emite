/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
