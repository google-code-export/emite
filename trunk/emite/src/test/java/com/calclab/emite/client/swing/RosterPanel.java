package com.calclab.emite.client.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.calclab.emite.client.im.roster.RosterItem;

@SuppressWarnings("serial")
public class RosterPanel extends JPanel {

    public static class RosterListItem {
	final RosterItem item;
	final String name;

	public RosterListItem(final String name, final RosterItem item) {
	    this.item = item;
	    this.name = name;
	}

	@Override
	public String toString() {
	    return name;
	}
    }

    public static interface RosterPanelListener {
	void onStartChat(RosterItem item);
    }

    private DefaultListModel model;

    public RosterPanel(final RosterPanelListener listener) {
	super(new BorderLayout());
	init(listener);
    }

    public void add(final String name, final RosterItem item) {
	model.addElement(new RosterListItem(name, item));
    }

    private void init(final RosterPanelListener listener) {
	model = new DefaultListModel();
	final JList list = new JList(model);
	list.addListSelectionListener(new ListSelectionListener() {
	    public void valueChanged(final ListSelectionEvent e) {
	    }
	});
	this.add(new JScrollPane(list), BorderLayout.CENTER);
	final JButton btnChat = new JButton("chat");
	btnChat.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		final Object value = list.getSelectedValue();
		if (value != null) {
		    final RosterListItem item = (RosterListItem) value;
		    listener.onStartChat(item.item);
		}
	    }
	});
	final JPanel buttons = new JPanel();
	buttons.add(btnChat);
	add(buttons, BorderLayout.SOUTH);
    }

}
