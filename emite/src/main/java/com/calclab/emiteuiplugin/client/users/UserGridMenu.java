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
package com.calclab.emiteuiplugin.client.users;

import java.util.Iterator;

import com.calclab.emiteuiplugin.client.AbstractPresenter;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class UserGridMenu {
    private final Menu menu;
    private final AbstractPresenter presenter;

    public UserGridMenu(final AbstractPresenter presenter) {
        this.presenter = presenter;
        menu = new Menu();
    }

    public void showMenu(final EventObject e) {
        menu.showAt(e.getXY());
    }

    public void setMenuItemList(final UserGridMenuItemList list) {
        menu.removeAll();
        for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
            UserGridMenuItem<?> item = (UserGridMenuItem<?>) iterator.next();
            addMenuItem(item);
        }
    }

    public void addMenuItem(final UserGridMenuItem<?> menuOpt) {
        Item menuItem = new Item(menuOpt.getTitle());
        menuItem.setIconCls(menuOpt.getIconCls());
        menu.addItem(menuItem);
        menuItem.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                presenter.doAction(menuOpt.getEventName(), menuOpt.getParam());
            }
        });
    }

}
