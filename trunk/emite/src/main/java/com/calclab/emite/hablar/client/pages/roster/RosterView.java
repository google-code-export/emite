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

import com.calclab.emite.hablar.client.PageView;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RosterView extends PageView {
    private final VerticalPanel list;
    private final VerticalPanel notifications;
    private final Event0 onChat;
    private final Event<String> onAddItem;
    private final Event0 onRemove;

    public RosterView() {
	onAddItem = new Event<String>("rosterView:onAddItem");
	onChat = new Event0("rosterView:onChat");
	onRemove = new Event0("rosterView:onRemove");

	createNewContactPanel();

	notifications = new VerticalPanel();
	add(notifications, DockPanel.NORTH);

	list = new VerticalPanel();
	addContent(list);
    }

    public void addItem(final RosterItemView itemView) {
	list.add(itemView);
    }

    public void addNotification(final SubscriptionRequestedPanel notification) {
	notifications.add(notification);
    }

    public void clearItems() {
	list.clear();
    }

    public void onAddItem(final Listener<String> listener) {
	onAddItem.add(listener);
    }

    public void onChat(final Listener0 listener) {
	onChat.add(listener);
    }

    public void onRemove(final Listener0 listener) {
	onRemove.add(listener);
    }

    public void removeItem(final RosterItemView itemView) {
	list.remove(itemView);
    }

    public void removeNotification(final SubscriptionRequestedPanel panel) {
	notifications.remove(panel);
    }

    private FlowPanel createNewContactPanel() {
	final FlowPanel newContactPanel = getToolbar();
	final TextBox fieldAddContact = new TextBox();
	newContactPanel.add(fieldAddContact);
	final Button btnAddContact = new Button("add", new ClickHandler() {
	    public void onClick(final ClickEvent event) {
		onAddItem.fire(fieldAddContact.getText());
		fieldAddContact.setText("");
	    }
	});
	newContactPanel.add(btnAddContact);
	return newContactPanel;
    }

}
