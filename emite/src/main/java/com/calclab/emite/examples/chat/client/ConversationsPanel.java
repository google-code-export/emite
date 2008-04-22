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
package com.calclab.emite.examples.chat.client;

import java.util.ArrayList;
import java.util.Collection;

import com.calclab.emite.client.im.roster.RosterItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConversationsPanel extends DockPanel {

    public static interface ConversationsListener {
	void onBeginChat(String jid);

	void onLogout();
    }

    private Button btnChat;
    private final TabPanel conversations;
    private final ArrayList<String> jids;
    private ListBox roster;
    private final Label status;

    public ConversationsPanel(final ConversationsListener listener) {
	jids = new ArrayList<String>();
	addStyleName("chatexample-conversations");
	conversations = new TabPanel();
	add(conversations, DockPanel.CENTER);

	status = new Label();
	final Button btnLogout = new Button("logout", new ClickListener() {
	    public void onClick(Widget sender) {
		listener.onLogout();
	    }
	});
	final FlowPanel logoutPanel = new FlowPanel();
	logoutPanel.add(btnLogout);
	add(logoutPanel, DockPanel.NORTH);
	final HorizontalPanel statusPanel = new HorizontalPanel();
	statusPanel.add(new Label("status: "));
	statusPanel.add(status);
	add(statusPanel, DockPanel.SOUTH);

	createRoster(listener);
    }

    public void addChat(final String jid, final ChatPanel chatPanel) {
	jids.add(jid);
	conversations.add(chatPanel, jid);
    }

    public void clearRoster() {
	roster.clear();
	btnChat.setEnabled(false);
    }

    public void removeChat(final String jid, final ChatPanel panel) {
	jids.remove(jid);
	conversations.remove(panel);
    }

    public void setRoster(final Collection<RosterItem> items) {
	clearRoster();
	for (final RosterItem item : items) {
	    roster.addItem(item.getName(), item.getJID().toString());
	}
    }

    public void setStatus(final String value) {
	status.setText(value);
    }

    public void show(final String jid, final ChatPanel chatPanel) {
	final int index = jids.indexOf(jid);
	GWT.log("Select tab: " + index, null);
	conversations.selectTab(index);
    }

    private void createRoster(final ConversationsListener listener) {
	final VerticalPanel panel = new VerticalPanel();
	roster = new ListBox();
	roster.setVisibleItemCount(10);
	roster.addChangeListener(new ChangeListener() {
	    public void onChange(final Widget sender) {
		btnChat.setEnabled(roster.getSelectedIndex() != -1);
	    }
	});
	panel.add(roster);
	btnChat = new Button("chat", new ClickListener() {
	    public void onClick(final Widget sender) {
		final int index = roster.getSelectedIndex();
		listener.onBeginChat(roster.getValue(index));
	    }

	});
	btnChat.setEnabled(false);
	panel.add(btnChat);
	add(panel, DockPanel.EAST);
    }
}
