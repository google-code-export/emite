package com.calclab.examplechat.client.chatuiplugin.users;

import java.util.ArrayList;
import java.util.Iterator;

public class UserGridMenuItemList {

    private final ArrayList<UserGridMenuItem<?>> list;

    public UserGridMenuItemList() {
        list = new ArrayList<UserGridMenuItem<?>>();
    }

    public void addItem(final UserGridMenuItem<?> item) {
        list.add(item);
    }

    public ArrayList<UserGridMenuItem<?>> getList() {
        return list;
    }

    public Iterator<UserGridMenuItem<?>> iterator() {
        return list.iterator();
    }
}
