package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.j2se.swing.ChatPanel.ChatPanelListener;

@SuppressWarnings("serial")
public class ConversationsPanel extends JPanel {
    private final JTabbedPane tabs;
    private final HashMap<String, ChatPanel> panels;

    public ConversationsPanel() {
	super(new BorderLayout());
	this.tabs = new JTabbedPane();
	this.add(new JScrollPane(tabs));
	this.panels = new HashMap<String, ChatPanel>();
    }

    public void close(final String id) {
	tabs.remove(panels.get(id));
    }

    public ChatPanel createChat(final String title, final String id, final ChatPanelListener listener) {
	final ChatPanel panel = new ChatPanel(listener);
	return addChat(title, id, panel);
    }

    public RoomPanel createRoom(final XmppURI uri, final String id, final ChatPanelListener listener) {
	final RoomPanel panel = new RoomPanel(listener);
	return (RoomPanel) addChat(uri.toString(), id, panel);
    }

    private ChatPanel addChat(final String title, final String id, final ChatPanel panel) {
	panels.put(id, panel);
	tabs.addTab(title, panel);
	return panel;
    }
}
