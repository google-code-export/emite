package com.calclab.emite.hablar.client.pages.roster;

import com.calclab.emite.hablar.client.PageView;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Event0;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener0;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RosterView extends PageView {
    private final VerticalPanel list;
    private final VerticalPanel notifications;
    private final Event0 onChat;
    private final Event<String> onAddItem;
    private final Event0 onRemove;

    public RosterView() {
	onAddItem = new Event<String>("rosterView:onAddItem");
	onChat = new Event0("rosterView:onChat");
	onRemove = new Event0("rosterView:onRemove");

	createNewContactPanel();

	notifications = new VerticalPanel();
	add(notifications, DockPanel.NORTH);

	list = new VerticalPanel();
	addContent(list);
    }

    public void addItem(final RosterItemView itemView) {
	list.add(itemView);
    }

    public void addNotification(final SubscriptionRequestedPanel notification) {
	notifications.add(notification);
    }

    public void clearItems() {
	list.clear();
    }

    public void onAddItem(final Listener<String> listener) {
	onAddItem.add(listener);
    }

    public void onChat(final Listener0 listener) {
	onChat.add(listener);
    }

    public void onRemove(final Listener0 listener) {
	onRemove.add(listener);
    }

    public void removeItem(final RosterItemView itemView) {
	list.remove(itemView);
    }

    public void removeNotification(final SubscriptionRequestedPanel panel) {
	notifications.remove(panel);
    }

    private FlowPanel createNewContactPanel() {
	final FlowPanel newContactPanel = getToolbar();
	final TextBox fieldAddContact = new TextBox();
	newContactPanel.add(fieldAddContact);
	final Button btnAddContact = new Button("add", new ClickListener() {
	    public void onClick(final Widget arg0) {
		onAddItem.fire(fieldAddContact.getText());
		fieldAddContact.setText("");
	    }
	});
	newContactPanel.add(btnAddContact);
	return newContactPanel;
    }

}
