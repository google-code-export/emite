package com.calclab.emite.hablar.client.pages;

import com.calclab.suco.client.listener.Event0;
import com.calclab.suco.client.listener.Listener0;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoggerPage extends DockPanel {
    private final VerticalPanel output;
    private final ScrollPanel scrollPanel;
    private final Event0 onClear;

    public LoggerPage() {
	onClear = new Event0("loggerView:onClear");

	final FlowPanel toolbar = new FlowPanel();
	toolbar.addStyleName("toolbar");
	final Button btnClear = new Button("Clear", new ClickListener() {
	    public void onClick(final Widget arg0) {
		onClear.fire();
	    }
	});
	toolbar.add(btnClear);
	add(toolbar, DockPanel.NORTH);

	output = new VerticalPanel();
	scrollPanel = new ScrollPanel(output);
	scrollPanel.setAlwaysShowScrollBars(true);
	add(scrollPanel, DockPanel.CENTER);
    }

    public void clearStanzas() {
	output.clear();
    }

    public void onClear(final Listener0 listener) {
	onClear.add(listener);
    }

    public void showReceived(final String stanza) {
	show(stanza, "received");
    }

    public void showSent(final String stanza) {
	show(stanza, "sent");
    }

    private void show(final String stanza, final String style) {
	final Label label = new Label(stanza);
	label.addStyleName(style);
	output.add(label);
    }

}
