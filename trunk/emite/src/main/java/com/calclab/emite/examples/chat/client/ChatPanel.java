package com.calclab.emite.examples.chat.client;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
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
	add(messages, DockPanel.CENTER);
	final DockPanel send = new DockPanel();
	send.add(new Label("message: "), DockPanel.WEST);
	input = new TextBox();
	send.add(input, DockPanel.CENTER);
	final Button btnSend = new Button("send", new ClickListener() {
	    public void onClick(Widget sender) {
		String text = input.getText();
		if (text.length() > 0) {
		    listener.onSend(text);
		    input.setText("");
		}
		input.setFocus(true);
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
}
