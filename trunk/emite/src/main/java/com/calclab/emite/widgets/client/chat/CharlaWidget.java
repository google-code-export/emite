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
package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.widgets.client.base.ComposedWidget;
import com.calclab.emite.widgets.client.base.DockableWidget;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.logout.LogoutWidget;

/**
 * CharlaWidget is a one to one chat with login
 */
public class CharlaWidget extends ComposedWidget {
    public CharlaWidget(final LoginWidget login, final ChatWidget chat, final LogoutWidget logout) {
	super(login, chat);
	chat.dock(DockableWidget.EXT_TOP, logout);
    }

    @Override
    protected String[] getExtraParamNames() {
	return null;
    }

    @Override
    protected void setExtraParam(final String name, final String value) {
    }

}
