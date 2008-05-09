package com.calclab.uimite.client.chat;

import com.calclab.emite.client.xmpp.session.Session.State;
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

    private final StatusViewListener listener;
    private final ListBox selector;
    private final Label labelState;

    public StatusView(final StatusViewListener listener) {
	this.selector = new ListBox(false);
	selector.addItem("login", "login");
	selector.addItem("logout", "logout");

	selector.addChangeListener(new ChangeListener() {
	    public void onChange(final Widget sender) {
		listener.onStatusChanged(selector.getItemText(selector.getSelectedIndex()));
	    }
	});

	this.labelState = new Label("welcome.");
	this.add(selector);
	this.add(labelState);
	this.listener = listener;
    }

    public void setState(final State state) {
	labelState.setText("state: " + state);
    }
}
