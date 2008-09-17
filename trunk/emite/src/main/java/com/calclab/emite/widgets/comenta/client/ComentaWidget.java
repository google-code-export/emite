package com.calclab.emite.widgets.comenta.client;

import java.util.Map;

import com.calclab.emite.browser.client.HasProperties;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Listener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ComentaWidget extends DockPanel implements HasProperties {
    private final Event<Map<String, String>> onSetProperties;
    private final Event<String> onMessage;
    private final HTML output;
    private final TextBox input;
    private final Button send;

    public ComentaWidget() {
	addStyleName("emite-widgets-Comenta");
	this.onSetProperties = new Event<Map<String, String>>("comenta:onSetProperty");
	this.onMessage = new Event<String>("comenta:onMessage");
	this.output = new HTML();
	output.addStyleName("output");
	this.input = new TextBox();
	this.send = new Button("send", new ClickListener() {
	    public void onClick(final Widget sender) {
		onMessage.fire(input.getText());
		input.setText("");
	    }
	});

	add(output, DockPanel.CENTER);
	final HorizontalPanel bottomPanel = new HorizontalPanel();
	bottomPanel.add(input);
	bottomPanel.add(send);
	bottomPanel.addStyleName("input");
	add(bottomPanel, DockPanel.SOUTH);
    }

    public String[] getPropertyNames() {
	return new String[] { "room" };
    }

    public void onMessage(final Listener<String> listener) {
	onMessage.add(listener);
    }

    public void onSetProperties(final Listener<Map<String, String>> listener) {
	onSetProperties.add(listener);
    }

    public void setEnabled(final boolean enabled) {
	input.setEnabled(enabled);
	send.setEnabled(enabled);
    }

    public void setProperties(final Map<String, String> properties) {
	onSetProperties.fire(properties);
    }

    public void show(final String name, final String body) {
	final String userClass = "user";
	final String user = name != null ? "<span class=\"" + userClass + "\">" + name + "</span>: " : "";
	final String line = "<div>" + user + body + "</div>";
	output.setHTML(output.getHTML() + line);
    }

}
