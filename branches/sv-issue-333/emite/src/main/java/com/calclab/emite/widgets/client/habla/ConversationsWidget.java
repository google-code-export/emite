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
package com.calclab.emite.widgets.client.habla;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.emite.widgets.client.chat.ChatWidget;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConversationsWidget extends TabPanel implements EmiteWidget {

    public ConversationsWidget() {
    }

    public void add(final String label, final ChatWidget widget) {
	this.add((Widget) widget, label);
    }

    public String[] getParamNames() {
	return null;
    }

    public void setParam(final String name, final String value) {
    }

}
