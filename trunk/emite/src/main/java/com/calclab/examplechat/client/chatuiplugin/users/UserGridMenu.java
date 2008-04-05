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
