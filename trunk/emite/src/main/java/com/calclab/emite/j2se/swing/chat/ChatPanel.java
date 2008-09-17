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
package com.calclab.emite.j2se.swing.chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ChatPanel extends JPanel {
    public static interface ChatPanelListener {
	void onClose(ChatPanel source);

	void onSend(ChatPanel source, String text);
    }

    private final JTextArea area;
    private final JTextField fieldBody;

    public ChatPanel(final ChatPanelListener listener) {
	super(new BorderLayout());
	this.area = new JTextArea();
	add(new JScrollPane(area));

	final ActionListener actionListener = new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		listener.onSend(ChatPanel.this, fieldBody.getText());
	    }
	};

	fieldBody = new JTextField();
	final JButton btnSend = new JButton("send");
	fieldBody.addActionListener(actionListener);
	btnSend.addActionListener(actionListener);

	final JButton btnClose = new JButton("close");
	btnClose.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		listener.onClose(ChatPanel.this);
	    }
	});

	final JPanel panel = new JPanel(new BorderLayout());
	panel.add(fieldBody);

	final JPanel buttons = new JPanel();
	buttons.add(btnSend);
	buttons.add(btnClose);

	panel.add(buttons, BorderLayout.EAST);
	add(panel, BorderLayout.SOUTH);

    }

    public void clearMessage() {
	fieldBody.setText("");
	fieldBody.requestFocus();
    }

    public void showIcomingMessage(final String from, final String body) {
	print(from + ": " + body);
    }

    public void showOutMessage(final String body) {
	print("me: " + body);
    }

    private void print(final String message) {
	area.setText(area.getText() + "\n" + message);
    }

}
