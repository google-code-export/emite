package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.j2se.swing.ChatPanel.ChatPanelListener;

@SuppressWarnings("serial")
public class ConversationsPanel extends JPanel {
    private final JTabbedPane tabs;

    public ConversationsPanel() {
	super(new BorderLayout());
	this.tabs = new JTabbedPane();
	this.add(new JScrollPane(tabs));
    }

    public ChatPanel createChat(final String title, final ChatPanelListener listener) {
	final ChatPanel panel = new ChatPanel(listener);
	tabs.addTab(title, panel);
	return panel;
    }

    public RoomPanel createRoom(final XmppURI uri, final ChatPanelListener listener) {
	final RoomPanel panel = new RoomPanel(listener);
	tabs.addTab(uri.toString(), panel);
	return panel;
    }
}
