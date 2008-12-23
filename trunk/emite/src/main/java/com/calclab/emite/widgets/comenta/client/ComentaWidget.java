package com.calclab.emite.widgets.comenta.client;

import java.util.Map;

import com.calclab.emite.browser.client.HasProperties;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class ComentaWidget extends AbsolutePanel implements HasProperties {
    private final Event<Map<String, String>> onSetProperties;
    private final Event<String> onMessage;
    private final HTML output;
    private final TextArea input;
    private final Label status;
    private final ScrollPanel scroll;

    public ComentaWidget() {
	this.onSetProperties = new Event<Map<String, String>>("comenta:onSetProperty");
	this.onMessage = new Event<String>("comenta:onMessage");

	this.status = new Label();
	this.output = new HTML();
	this.scroll = new ScrollPanel(output);
	this.input = new TextArea();
	input.addKeyboardListener(new KeyboardListener() {
	    public void onKeyDown(final Widget sender, final char keyCode, final int modifiers) {
	    }

	    public void onKeyPress(final Widget sender, final char keyCode, final int modifiers) {
		if (keyCode == 13) {
		    onMessage.fire(input.getText().trim());
		    input.setText(null);
		    input.setCursorPos(1);
		    input.setFocus(true);
		}
	    }

	    public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
	    }

	});

	initLayout();
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
    }

    public void setProperties(final Map<String, String> properties) {
	onSetProperties.fire(properties);
    }

    public void show(final String name, final String body) {
	final String userClass = "user";
	final String user = name != null ? "<span class=\"" + userClass + "\">" + name + "</span>: " : "";
	final String line = "<div>" + user + body + "</div>";
	output.setHTML(output.getHTML() + line);
	scroll.scrollToBottom();
    }

    public void showStatus(final String message, final String cssClass) {
	status.setText(message);
    }

    private void initLayout() {
	final int width = 300;
	final int height = 300;
	this.setPixelSize(width, height);
	this.setStylePrimaryName("emite-widgets-Comenta");

	final Label top = new Label();
	add(top, 0, 0);
	top.setPixelSize(width, 30);
	top.setStylePrimaryName("top");
	add(status, 10, 10);
	status.setPixelSize(width, 35);

	final Label middle = new Label();
	add(middle, 0, 31);
	middle.setPixelSize(width, height - 85);
	middle.setStylePrimaryName("middle");
	add(scroll, 0, 32);
	scroll.setPixelSize(width, height - 85);

	final Label bottom = new Label();
	add(bottom, 0, height - 51);
	bottom.setPixelSize(width, 51);
	bottom.setStylePrimaryName("bottom");
	add(input, 10, height - 43);
	input.setPixelSize(width - 20, 30);
    }

}
