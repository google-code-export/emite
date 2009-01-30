/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuimodule.client.users;

import java.util.Iterator;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class UserGridMenu {
    private final Menu menu;

    public UserGridMenu() {
        menu = new Menu();
    }

    public void addMenuItem(final UserGridMenuItem<?> menuOpt) {
        Item menuItem = new Item(menuOpt.getTitle());
        menuItem.setIconCls(menuOpt.getIconCls());
        menu.addItem(menuItem);
        menuItem.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        menuOpt.getListener().onAction();
                    }
                });
            }
        });
    }

    public void setMenuItemList(final UserGridMenuItemList list) {
        menu.removeAll();
        for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
            UserGridMenuItem<?> item = (UserGridMenuItem<?>) iterator.next();
            addMenuItem(item);
        }
    }

    public void showMenu(final EventObject e) {
        menu.showAt(e.getXY());
    }

}
