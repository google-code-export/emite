package com.calclab.emite.examples.chat.client;

import java.util.Collection;

import com.calclab.emite.client.im.roster.RosterItem;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConversationsPanel extends DockPanel {

    public static interface ConversationsListener {

	void onBeginChat(String jid);

	void onLogout();

    }

    private Button btnChat;
    private final TabPanel conversations;
    private ListBox roster;
    private final Label status;

    public ConversationsPanel(final ConversationsListener listener) {
	addStyleName("chatexample-conversations");
	conversations = new TabPanel();
	add(conversations, DockPanel.CENTER);

	status = new Label();
	final Button btnLogout = new Button("logout", new ClickListener() {
	    public void onClick(Widget sender) {
		listener.onLogout();
	    }
	});
	final FlowPanel flow = new FlowPanel();
	flow.add(btnLogout);
	flow.add(new Label("status: "));
	flow.add(status);
	add(flow, DockPanel.SOUTH);

	createRoster(listener);
    }

    public void addChat(final String jid, final ChatPanel chatPanel) {
	conversations.add(chatPanel, jid);
    }

    public void clearRoster() {
	roster.clear();
    }

    public void removeChat(final ChatPanel panel) {
	conversations.remove(panel);
    }

    public void setRoster(final Collection<RosterItem> items) {
	clearRoster();
	for (final RosterItem item : items) {
	    roster.addItem(item.getName(), item.getJID().toString());
	}
    }

    public void setStatus(final String value) {
	status.setText(value);
    }

    public void show(final String jid, final ChatPanel chatPanel) {

    }

    private void createRoster(final ConversationsListener listener) {
	final VerticalPanel panel = new VerticalPanel();
	roster = new ListBox();
	roster.setVisibleItemCount(10);
	roster.addChangeListener(new ChangeListener() {
	    public void onChange(final Widget sender) {
		btnChat.setEnabled(roster.getSelectedIndex() != -1);
	    }
	});
	panel.add(roster);
	btnChat = new Button("chat", new ClickListener() {
	    public void onClick(final Widget sender) {
		final int index = roster.getSelectedIndex();
		listener.onBeginChat(roster.getValue(index));
	    }

	});
	btnChat.setEnabled(false);
	panel.add(btnChat);
	add(panel, DockPanel.EAST);
    }
}
