package com.calclab.emite.widgets.client.chat;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class GWTAbstractChatWidget extends DockPanel implements AbstractChatWidget {
    private final Label status;
    private final TextArea area;
    private final TextBox input;
    private final Button send;
    protected Signal<String> onSendMessage;

    public GWTAbstractChatWidget() {
	this.onSendMessage = new Signal<String>("widgets:room:sendMessage");
	this.status = new Label();
	this.area = new TextArea();
	this.input = new TextBox();
	input.addKeyboardListener(new KeyboardListener() {
	    public void onKeyDown(final Widget sender, final char keyCode, final int modifiers) {
	    }

	    public void onKeyPress(final Widget sender, final char keyCode, final int modifiers) {
		if (keyCode == 13) {
		    sendMessage();
		}
	    }

	    public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
	    }

	});

	this.send = new Button("send", new ClickListener() {
	    public void onClick(final Widget sender) {
		sendMessage();
	    }
	});
	final HorizontalPanel inputBar = new HorizontalPanel();
	inputBar.add(input);
	inputBar.add(send);

	add(status, DockPanel.NORTH);
	add(area, DockPanel.CENTER);
	add(inputBar, DockPanel.SOUTH);
    }

    public void addWidget(final DockLayoutConstant layoutConstant, final EmiteWidget widget) {
	assert layoutConstant != CENTER;

	add((Widget) widget, layoutConstant);
    }

    public void onSendMessage(final Slot<String> slot) {
	onSendMessage.add(slot);
    }

    public void setInputEnabled(final boolean enabled) {
	Log.debug("AbstractChat set input enabled: " + enabled);
	area.setEnabled(enabled);
	input.setEnabled(enabled);
	send.setEnabled(enabled);
    }

    public void setStatus(final String message) {
	status.setText(message);
    }

    public void write(final String from, final String message) {
	final String text = area.getText();
	final String prefix = from != null ? from + ": " : "";
	area.setText(text + prefix + message + "\n");
    }

    private void sendMessage() {
	final String text = input.getText();
	if (text.length() > 0) {
	    onSendMessage.fire(text);
	    input.setText("");
	    input.setFocus(true);
	}
    }

}
