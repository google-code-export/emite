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
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AddRosterItemPanel extends JPanel {
    public static interface AddRosterItemPanelListener {

	void onCancel();

	void onCreate(String uri, String name);

    }

    private JTextField fieldURI;
    private JTextField fieldName;

    public AddRosterItemPanel(final AddRosterItemPanelListener listener) {
	super(new BorderLayout());
	init(listener);
    }

    private Component createRow(final String label, final JTextField field) {
	final JPanel panel = new JPanel(new BorderLayout());
	panel.add(new JLabel(label), BorderLayout.WEST);
	panel.add(field);
	return panel;
    }

    private void init(final AddRosterItemPanelListener listener) {

	fieldURI = new JTextField();
	fieldName = new JTextField();

	final JPanel form = new JPanel(new GridLayout(2, 1));
	form.add(createRow("URI:", fieldURI));
	form.add(createRow("Name:", fieldName));
	add(form, BorderLayout.CENTER);

	final JButton btnOk = new JButton("Ok");
	btnOk.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		listener.onCreate(fieldURI.getText(), fieldName.getText());
	    }
	});

	final JButton btnCancel = new JButton("Cancel");
	btnCancel.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		listener.onCancel();
	    }
	});
	final JPanel buttons = new JPanel();
	buttons.add(btnOk);
	buttons.add(btnCancel);
	add(buttons, BorderLayout.SOUTH);
    }
}
