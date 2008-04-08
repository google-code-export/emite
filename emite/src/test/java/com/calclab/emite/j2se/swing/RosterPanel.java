package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.j2se.swing.AddRosterItemPanel.AddRosterItemPanelListener;

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
	    return name + "(" + item.getXmppURI() + ") " + item.getPresenceType();
	}
    }

    public static interface RosterPanelListener {
	void onAddRosterItem(String uri, String name);

	void onRemoveItem(RosterItem item);

	void onStartChat(RosterItem item);
    }

    private JPanel creationPanel;
    private JDialog currentDialog;
    private JList list;
    private final RosterPanelListener listener;
    private DefaultListModel model;

    public RosterPanel(final JFrame owner, final RosterPanelListener listener) {
	super(new BorderLayout());
	this.listener = listener;
	currentDialog = null;
	init(owner);
    }

    public void add(final String name, final RosterItem item) {
	model.addElement(new RosterListItem(name, item));
    }

    public void clear() {
	model.clear();
    }

    public void refresh() {
	list.repaint();
    }

    protected void closeDialog() {
	if (currentDialog != null) {
	    currentDialog.setVisible(false);
	}
    }

    protected JPanel getCreationPanel() {
	if (this.creationPanel == null) {
	    this.creationPanel = new AddRosterItemPanel(new AddRosterItemPanelListener() {
		public void onCancel() {
		    closeDialog();
		}

		public void onCreate(final String uri, final String name) {
		    closeDialog();
		    listener.onAddRosterItem(uri, name);
		}

	    });

	}
	return creationPanel;
    }

    private void init(final JFrame owner) {
	model = new DefaultListModel();
	list = new JList(model);
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

	final JButton btnAddContact = new JButton("add contact");

	btnAddContact.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		currentDialog = new JDialog(owner);
		currentDialog.setContentPane(getCreationPanel());
		currentDialog.setModal(true);
		currentDialog.pack();
		currentDialog.setVisible(true);
	    }
	});

	final JButton btnRemoveContact = new JButton("remove contact");
	btnRemoveContact.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		final RosterListItem item = (RosterListItem) list.getSelectedValue();
		if (item != null) {
		    listener.onRemoveItem(item.item);
		}
	    }

	});

	final JPanel buttons = new JPanel(new GridLayout(3, 1));
	buttons.add(btnChat);
	buttons.add(btnAddContact);
	buttons.add(btnRemoveContact);
	add(buttons, BorderLayout.SOUTH);
    }

}
