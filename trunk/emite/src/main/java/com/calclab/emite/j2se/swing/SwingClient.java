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
package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.calclab.emite.j2se.swing.chat.ConversationsPanel;
import com.calclab.emite.j2se.swing.log.LogPanel;
import com.calclab.emite.j2se.swing.login.LoginPanel;
import com.calclab.emite.j2se.swing.roster.RosterPanel;

public class SwingClient {

    public SwingClient(final JFrame frame, final LoginPanel loginPanel, final RosterPanel rosterPanel,
	    final ConversationsPanel conversationsPanel, final LogPanel logPanel) {

	final JPanel root = new JPanel(new BorderLayout());
	addXmppListeners();

	final JLabel status = new JLabel("emite test client");

	root.add(loginPanel, BorderLayout.NORTH);
	root.add(conversationsPanel, BorderLayout.CENTER);
	final JPanel south = new JPanel(new BorderLayout());
	south.add(status, BorderLayout.NORTH);
	south.add(logPanel, BorderLayout.CENTER);
	root.add(south, BorderLayout.SOUTH);

	final JTabbedPane tabs = new JTabbedPane();
	tabs.add("chats", rosterPanel);

	root.add(tabs, BorderLayout.EAST);

	frame.setContentPane(root);

    }

    private void addXmppListeners() {

    }

}
