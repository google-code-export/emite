package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.calclab.emite.client.extra.muc.Occupant;

@SuppressWarnings("serial")
public class RoomPanel extends ChatPanel {

    private final DefaultListModel users;
    private final JList usersList;

    public RoomPanel(final ChatPanelListener listener) {
	super(listener);
	users = new DefaultListModel();
	usersList = new JList(users);
	add(usersList, BorderLayout.EAST);
    }

    public void setUsers(final Collection<Occupant> users) {
	this.users.clear();
	for (final Occupant user : users) {
	    this.users.addElement(user);
	}
    }

}
