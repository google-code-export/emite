package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.emite.widgets.client.base.GWTExtensibleWidget;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class GWTAbstractChatWidget extends GWTExtensibleWidget implements AbstractChatWidget {
    private final TextArea area;
    private final TextBox input;
    private final Button send;
    protected Event<String> onSendMessage;
    private AbstractChatController controller;

    public GWTAbstractChatWidget() {
	this.onSendMessage = new Event<String>("widgets:room:sendMessage");
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

	add(area, DockPanel.CENTER);
	add(inputBar, DockPanel.SOUTH);
    }

    public void addWidget(final DockLayoutConstant layoutConstant, final EmiteWidget widget) {
	assert layoutConstant != CENTER;

	add((Widget) widget, layoutConstant);
    }

    public AbstractChatController getController() {
	return controller;
    }

    public void onSendMessage(final Listener<String> listener) {
	onSendMessage.add(listener);
    }

    public void setChat(final Chat chat) {
	controller.setChat(chat);
    }

    public void setController(final AbstractChatController controller) {
	this.controller = controller;
    }

    public void setInputEnabled(final boolean enabled) {
	area.setEnabled(enabled);
	input.setEnabled(enabled);
	send.setEnabled(enabled);
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
