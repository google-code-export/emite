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
package com.calclab.emite.widgets.client.logout;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.suco.client.events.Event0;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LogoutWidget extends HorizontalPanel implements EmiteWidget {

    private final Label label;
    private final Button button;
    final Event0 onLogout;

    public LogoutWidget() {
	setStylePrimaryName("emite-LogoutWidget");
	this.onLogout = new Event0("widgets:logout:onLogout");
	this.label = new Label();
	this.button = new Button("logout", new ClickListener() {
	    public void onClick(final Widget sender) {
		onLogout.fire();
	    }
	});
	add(label);
	add(button);
    }

    public String[] getParamNames() {
	return null;
    }

    public void setButtonVisible(final boolean visible) {
	button.setVisible(visible);
    }

    public void setParam(final String name, final String value) {
    }

    public void showMessage(final String message) {
	label.setText(message);
    }

}
