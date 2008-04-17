package com.calclab.emite.examples.chat.client;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatPanel extends DockPanel {

    public static interface ChatPanelListener {
	void onSend(String text);
    }

    private final TextBox input;
    private final VerticalPanel messages;

    public ChatPanel(final ChatPanelListener listener) {
	addStyleName("chatexample-chat");
	messages = new VerticalPanel();
	final ScrollPanel scroll = new ScrollPanel(messages);
	add(scroll, DockPanel.CENTER);
	final DockPanel send = new DockPanel();
	send.add(new Label("message: "), DockPanel.WEST);
	input = new TextBox();
	input.addKeyboardListener(new KeyboardListener() {
	    public void onKeyDown(final Widget sender, final char keyCode, final int modifiers) {
	    }

	    public void onKeyPress(final Widget sender, final char keyCode, final int modifiers) {
		GWT.log("KEY code: " + keyCode, null);
	    }

	    public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
	    }

	});
	send.add(input, DockPanel.CENTER);
	final Button btnSend = new Button("send", new ClickListener() {
	    public void onClick(Widget sender) {
		send(listener);
	    }
	});
	send.add(btnSend, DockPanel.EAST);
	input.setFocus(true);
	add(send, DockPanel.SOUTH);
    }

    public void showIncomingMessage(final XmppURI fromURI, final String body) {
	messages.add(new Label(fromURI.toString() + ": " + body));
    }

    public void showOutcomingMessage(final String body) {
	messages.add(new Label("me: " + body));
    }

    private void send(final ChatPanelListener listener) {
	final String text = input.getText();
	if (text.length() > 0) {
	    listener.onSend(text);
	    input.setText("");
	}
	input.setFocus(true);
    }
}
