package com.calclab.examplechat.client.chatuiplugin.users;

import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class UserGridMenu {
    private final Menu menu;
    private final MultiChatPresenter presenter;

    public UserGridMenu(final MultiChatPresenter presenter) {
        this.presenter = presenter;
        menu = new Menu();
    }

    public void addMenuOption(final String title, final String iconCls, final String eventId, final Object param) {
        Item menuItem = new Item(title);
        menuItem.setIconCls(iconCls);
        menu.addItem(menuItem);
        menuItem.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                presenter.doAction(eventId, param);
            }
        });
    }

    public void showMenu(final EventObject e) {
        menu.showAt(e.getXY());
    }
}
