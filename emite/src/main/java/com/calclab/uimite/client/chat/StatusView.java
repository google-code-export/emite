package com.calclab.uimite.client.chat;

import java.util.ArrayList;

import com.calclab.uimite.client.UIView;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class StatusView extends FlowPanel implements UIView {

    public static interface StatusViewListener {
	void onStatusChanged(String status);
    }

    @SuppressWarnings("serial")
    private static class StatusViewListenerCollection extends ArrayList<StatusViewListener> implements
	    StatusViewListener {

	public void onStatusChanged(final String status) {
	    for (final StatusViewListener listener : this) {
		listener.onStatusChanged(status);
	    }
	}

    }

    private final ListBox selector;
    private final Label labelState;
    private final StatusViewListenerCollection listeners;

    public StatusView() {
	this.listeners = new StatusViewListenerCollection();
	this.selector = new ListBox(false);
	selector.addItem("login", "login");
	selector.addItem("logout", "logout");

	selector.addChangeListener(new ChangeListener() {
	    public void onChange(final Widget sender) {
		listeners.onStatusChanged(selector.getItemText(selector.getSelectedIndex()));
	    }
	});

	this.labelState = new Label("welcome.");
	this.add(selector);
	this.add(labelState);
    }

    public void addListener(final StatusViewListener listener) {
	listeners.add(listener);
    }

    public void showState(final String state) {
	labelState.setText(state);
    }
}
