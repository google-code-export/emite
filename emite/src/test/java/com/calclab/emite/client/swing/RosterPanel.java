package com.calclab.emite.client.swing;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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

	private DefaultListModel model;

	public RosterPanel() {
		super(new BorderLayout());
		init();
	}

	public void add(final String name, final RosterItem item) {
		model.addElement(new RosterListItem(name, item));
	}

	private void init() {
		model = new DefaultListModel();
		final JList list = new JList(model);
		this.add(new JScrollPane(list), BorderLayout.CENTER);
	}

}
