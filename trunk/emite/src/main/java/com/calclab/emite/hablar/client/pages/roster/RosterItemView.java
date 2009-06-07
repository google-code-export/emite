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
package com.calclab.emite.hablar.client.pages.roster;

import com.calclab.emite.im.client.roster.RosterItem;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;

public class RosterItemView extends FlowPanel {
    public static interface RosterItemViewListener {
	void onAction(RosterItemView view);
    }

    private RosterItem item;
    private final HTML show;
    private final Hyperlink jid;
    private final Hyperlink menu;

    public RosterItemView(final RosterItem item, final RosterItemViewListener listener) {
	setStyleName("hablar-RosterItemView");
	this.show = new HTML();
	this.jid = new Hyperlink();
	this.menu = new Hyperlink();
	menu.setText("menu");
	add(show);
	add(jid);
	add(menu);

	jid.addClickHandler(new ClickHandler() {
	    public void onClick(final ClickEvent event) {
		listener.onAction(RosterItemView.this);
	    }
	});

	setItem(item);
    }

    public RosterItem getItem() {
	return item;
    }

    public void setItem(final RosterItem item) {
	this.item = item;
	show.setHTML("<b>" + item.getShow() + "</b>");
	jid.setText(item.getJID().toString());
    }

}
