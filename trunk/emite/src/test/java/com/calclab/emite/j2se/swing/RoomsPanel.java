package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.calclab.emite.client.extra.muc.Room;

@SuppressWarnings("serial")
public class RoomsPanel extends JPanel {

    private static class RoomWrapper {
	private final Room room;

	public RoomWrapper(final Room room) {
	    this.room = room;
	}

	@Override
	public String toString() {
	    String name = room.getName();
	    name = name != null ? name : "(no name)";
	    final String jid = room.getJid();
	    return name + " - " + jid;
	}
    }

    private final JList list;
    private final RoomsPanelListener listener;

    private final DefaultListModel listModel;

    public RoomsPanel(final RoomsPanelListener listener) {
	super(new BorderLayout());
	this.listener = listener;

	final JButton btnEnter = new JButton("enter room");

	listModel = new DefaultListModel();
	list = new JList(listModel);
	add(list, BorderLayout.CENTER);

	final JPanel buttons = new JPanel(new GridLayout(2, 1));
	buttons.add(new JLabel());
	buttons.add(btnEnter);
	add(buttons, BorderLayout.SOUTH);
    }

    public void setRooms(final Collection<Room> rooms) {
	listModel.clear();
	for (final Room room : rooms) {
	    listModel.addElement(new RoomWrapper(room));
	}
    }

}
