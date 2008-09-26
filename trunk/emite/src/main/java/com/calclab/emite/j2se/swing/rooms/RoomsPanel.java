package com.calclab.emite.j2se.swing.rooms;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Listener;

public class RoomsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Event<String> onOpenRoom;

    public RoomsPanel(final JFrame frame) {
	super(new BorderLayout());
	this.onOpenRoom = new Event<String>("roomsPanel:openRoom");
	final JPanel verticalPanel = new JPanel(new GridLayout(3, 1));
	final JTextField roomURI = new JTextField("testroom@rooms.localhost/nick");
	verticalPanel.add(roomURI);
	final JButton btnOpen = new JButton("Open room");
	btnOpen.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onOpenRoom.fire(roomURI.getText());
	    }
	});
	verticalPanel.add(btnOpen);
	add(verticalPanel, BorderLayout.SOUTH);
    }

    public void onOpenRoom(final Listener<String> listener) {
	onOpenRoom.add(listener);
    }
}
