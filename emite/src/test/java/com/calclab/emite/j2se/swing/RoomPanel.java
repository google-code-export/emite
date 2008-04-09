package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.calclab.emite.client.extra.muc.RoomUser;

@SuppressWarnings("serial")
public class RoomPanel extends ChatPanel {

    private final DefaultListModel users;
    private final JList usersList;

    public RoomPanel(final ChatPanelListener listener) {
	super(listener);
	users = new DefaultListModel();
	usersList = new JList(users);
	add(usersList, BorderLayout.WEST);
    }

    public void setUsers(final Collection<RoomUser> users) {
	this.users.clear();
	for (final RoomUser user : users) {
	    this.users.addElement(users);
	}
    }

}
