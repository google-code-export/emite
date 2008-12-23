package com.calclab.emite.j2se.swing.log;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Listener0;

@SuppressWarnings("serial")
public class LogPanel extends JPanel {
    private final DefaultListModel model;
    private final Event0 onClear;

    public LogPanel() {
	super(new BorderLayout());

	this.onClear = new Event0("logPanel:clear");

	model = new DefaultListModel();
	add(new JScrollPane(new JList(model)), BorderLayout.CENTER);
	final JButton clear = new JButton("clear");
	clear.addActionListener(new ActionListener() {
	    public void actionPerformed(final ActionEvent e) {
		onClear.fire();
	    }
	});
	final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	panel.add(clear);
	add(panel, BorderLayout.NORTH);
    }

    public void clear() {
	model.removeAllElements();
    }

    public void onClear(final Listener0 listener) {
	onClear.add(listener);
    }

    public void showIncomingPacket(final IPacket packet) {
	model.addElement("RECEIVED: " + packet);
    }

    public void showOutcomingPacket(final IPacket packet) {
	model.addElement("SENT: " + packet);
    }

}
