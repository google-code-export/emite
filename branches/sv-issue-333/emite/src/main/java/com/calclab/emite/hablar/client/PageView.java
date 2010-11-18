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
package com.calclab.emite.hablar.client;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class PageView extends DockPanel {

    private FlowPanel toolbar;
    private final DeckPanel content;

    public PageView() {
	setStyleName("hablar-Page");
	content = new DeckPanel();
	final FlowPanel flow = new FlowPanel();
	flow.add(content);
	add(flow, DockPanel.CENTER);
    }

    public void addContent(final Widget w) {
	content.add(w);
	content.showWidget(content.getWidgetCount() - 1);
    }

    public FlowPanel getToolbar() {
	if (toolbar == null) {
	    toolbar = new FlowPanel();
	    toolbar.setStyleName("toolbar");
	    add(toolbar, DockPanel.NORTH);
	}
	return toolbar;
    }

    public void setToolbarVisible(final boolean visible) {
	getToolbar().setVisible(visible);
    }

    public void showContent(final int index) {
	content.showWidget(index);
    }

}
